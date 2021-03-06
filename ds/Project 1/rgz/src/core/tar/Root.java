package core.tar;

import core.dc.DCM;
import core.dc.SequentialThreadingPool;
import core.io.CountableBufferedOutputStream;
import core.io.MemoryMappedFileInputStream;
import core.notify.*;
import excs.DCException;
import excs.TarException;
import ui.Config;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Root is the final menu in the tree, and manage decompress/compress of the tree
 */
public class Root extends Menu {
    Map<RegularFile, String> regularFilePathsMap = new HashMap<>();

    public Root() {
        setName("");
        parent = null;
    }

    /**
     * clear up the root
     */
    public void reset() {
        regularFilePathsMap.clear();
        children.clear();
    }

    @Override
    public void dumpIndex(OutputStream out)
    throws IOException {
        assert out instanceof CountableBufferedOutputStream;
        ((CountableBufferedOutputStream) out).clearCounter();

        super.dumpIndex(out);

        // write index length at the end of file
        int indexLength = ((CountableBufferedOutputStream) out).getWroteBytes();
        for(int i = 0;i < 4;i ++)
            out.write(((indexLength >> (i * 8)) & 0xff));
    }

    @Override
    public void loadIndex(InputStream in)
    throws IOException, TarException {
        byte b = (byte)in.read();
        if(b != 127) { // root must be a menu!
            throw new TarException("cannot load index: wrong index format - root must be a menu");
        }
        super.loadIndex(in);

        // redundant bytes in the file
        if(in.available() != 0)
            throw new TarException("cannot load index: wrong index format - redundant bytes");
    }

    /**
     * load index from file
     * @param srcFile file name
     * @throws TarException
     */
    public void loadIndexFromFile(String srcFile)
    throws TarException{
        // create file channel
        FileChannel fc;
        try {
            fc = new FileInputStream(srcFile).getChannel();
        } catch (FileNotFoundException e) {
            throw new TarException("cannot load index: " + e.getMessage());
        }

        try {
            // read last four bytes
            long totSize = fc.size();
            fc.position(totSize - 4);

            ByteBuffer b = ByteBuffer.allocate(4);
            fc.read(b);

            // get index size
            int indexSize = 0;
            for(int i = 0;i < 4;i ++)
                indexSize |= (b.get(i) & 0xff) << (i * 8);

            // read in index
            b = ByteBuffer.allocate(indexSize);
            fc.position(totSize - 4 - indexSize);
            fc.read(b);

            // load index
            Notifier.getNotifier().addNotifyMessage(new MSGLoadingIndex());
            loadIndex(new ByteArrayInputStream(b.array()));
            Notifier.getNotifier().addNotifyMessage(new MSGLoadedIndex());
        } catch (IOException e) {
            throw new TarException("cannot load index: " + e.getMessage());
        }

        // close fc
        try {
            fc.close();
        } catch (IOException e) {
            reset();
            throw new TarException("cannot load index: " + e.getMessage());
        }
    }

    /**
     * add the file into the tree
     * @param srcFileName fiel name
     * @throws TarException
     */
    public void addSource(String srcFileName)
    throws TarException {
        Notifier.getNotifier().addNotifyMessage(new MSGAddingSource());
        Path srcPath = Paths.get(srcFileName);
        if(! srcPath.toFile().exists())
            throw new TarException("cannot found source file or directory " + srcFileName);

        FileNode cur;

        if(srcPath.toFile().isFile()) {
            cur = new RegularFile();
        } else {
            cur = new Menu();
        }

        cur.setName(srcPath.getFileName().toString());
        cur.parent = this;
        this.addFileNode(cur);

        if(cur instanceof RegularFile) { // this is a regular file
            Notifier.getNotifier().addNotifyMessage(new MSGTarBuildingIndex(cur.getPath()));
            regularFilePathsMap.put((RegularFile) cur, srcFileName);
        } else { // this is a directory
            try {
                discoverDirectory((Menu)cur, srcPath);
            } catch (IOException e) {
                throw new TarException("cannot discover directory: " + e.getMessage());
            }
        }

        Notifier.getNotifier().addNotifyMessage(new MSGAddedSource());
    }

    /**
     * discover the menu to add sources
     * @param m the menu
     * @param p the path of menu
     * @throws TarException
     * @throws IOException
     */
    private void discoverDirectory(Menu m, Path p)
    throws TarException, IOException {
        File dir = p.toFile();

        for(String f: dir.list()) {
            if(m.hasChild(f))
                continue;

            File cf = new File(dir, f);

            FileNode t;

            if(isSymlink(cf)) { // symbol link will take problems, ignored
                continue;
            }

            if(cf.isFile()) { // regular File
                t = new RegularFile();
                t.setName(f);
                t.parent = m;
                Notifier.getNotifier().addNotifyMessage(new MSGTarBuildingIndex(cf.getPath()));
                regularFilePathsMap.put((RegularFile)t, cf.getPath());
            } else if (cf.isDirectory()){
                t = new Menu();
                t.setName(f);
                t.parent = m;
                discoverDirectory((Menu)t, Paths.get(cf.getPath()));
            } else {// otherwise, ignored
                continue;
            }
            m.addFileNode(t);
        }
    }

    /**
     * test file is symbol link
     * @param file the file to test
     * @return true if is
     * @throws IOException
     */
    static boolean isSymlink(File file) throws IOException {
        if (file == null)
            throw new NullPointerException("File must not be null");
        File canon;
        if (file.getParent() == null) {
            canon = file;
        } else {
            File canonDir = file.getParentFile().getCanonicalFile();
            canon = new File(canonDir, file.getName());
        }
        return !canon.getCanonicalFile().equals(canon.getAbsoluteFile());
    }

    /**
     * compress the tree
     * @param outputFile output file
     * @param conf the config to do compress
     * @param nThread number of thread to do compress
     * @throws TarException
     * @throws DCException
     */
    public void compress(String outputFile, Config conf, int nThread)
    throws TarException, DCException {
        // create Output stream
        OutputStream out;
        try {
            out = new CountableBufferedOutputStream(new FileOutputStream(outputFile));
        } catch (FileNotFoundException e) {
            throw new TarException(e.getMessage());
        }

        // dump meta
        conf.dumpMeta(out);

        // index has build up
        // then compress files
        int tot = regularFilePathsMap.keySet().size(),
            findex = 0;
        // create tp
        SequentialThreadingPool tp = null;
        if(nThread >= 2)
            tp = new SequentialThreadingPool(nThread);

        for(RegularFile f: regularFilePathsMap.keySet()) {
            findex ++;
            Notifier.getNotifier().addNotifyMessage(new MSGDCMCompressNew(f.getPath(), findex, tot));

            InputStream in = null;
            int cFileSize = 0;
            try {
                FileInputStream fin = new FileInputStream(regularFilePathsMap.get(f));
                in = new BufferedInputStream(fin);
                cFileSize = fin.available();
            } catch (FileNotFoundException e) {
                // never reach here
                e.printStackTrace();
            } catch (IOException e) {
                throw new TarException("compress failed: " + e.getMessage());
            }

            // set the offset
            f.setDataOffset(((CountableBufferedOutputStream)out).getWroteBytes());
            int startOffset = ((CountableBufferedOutputStream) out).getWroteBytes();

            // compress
            conf.getDCM().compress(in, out, tp);

            int endOffset = ((CountableBufferedOutputStream) out).getWroteBytes();

            // set size
            f.compressedSize = endOffset - startOffset;
            f.size = cFileSize;
        }

        // join jobs
        if(tp != null)
            tp.joinAll();

        // close output stream
        try {
            // dump index to the very end of the file
            Notifier.getNotifier().addNotifyMessage(new MSGDumpingIndex());
            dumpIndex(out);
            out.close();
            Notifier.getNotifier().addNotifyMessage(new MSGDumpedIndex());
        } catch (IOException e) {
            throw new TarException(e.getMessage());
        }
    }

    /**
     * decompress the file in the tree
     * @param rootDirString the root dir to put output
     * @param fileNodes file nodes to decompress
     * @param srcFile source files
     * @throws TarException
     * @throws DCException
     */
    public void decompress(String rootDirString, FileNode[] fileNodes, String srcFile)
    throws TarException, DCException {
        // load dcm
        Config conf = new Config();
        try {
            conf.loadMeta(new FileInputStream(srcFile));
        } catch (FileNotFoundException e) {
            throw new TarException(e.getMessage());
        }

        // load root dir
        File rootDir = new File(rootDirString);

        if(! rootDir.exists()) {
            if(! rootDir.mkdir()) {
                throw new TarException("decompressed failed: cannot create output dir");
            }
        }

        File src = new File(srcFile);
        if(! src.exists())
            throw new TarException("decompress failed: cannot found src file " + srcFile);

        // decompress each file
        int tot = fileNodes.length,
            findex = 0;
        for(FileNode fn: fileNodes) {
            findex ++;
            Notifier.getNotifier().addNotifyMessage(new MSGDCMDecompressNew(fn.getPath(), findex, tot));

            try {
                doDecompress(rootDir, fn, conf.getDCM(), src);
            } catch (IOException e) {
                e.printStackTrace();
                throw new TarException("decompress failed: " + e.getMessage());
            }
        }

        Notifier.getNotifier().addNotifyMessage(new MSGDecompressDone());
    }


    /**
     * do true decompress
     * @param m parent menu
     * @param fn the file node to decompress
     * @param dcm dcm to do decompress
     * @param src source file
     * @throws IOException
     * @throws DCException
     * @throws TarException
     */
    private static void doDecompress(File m, FileNode fn, DCM dcm, File src)
    throws IOException, DCException, TarException {
        File f = new File(m, fn.getName());

        if(fn instanceof RegularFile) {
            Notifier.getNotifier().addNotifyMessage(new MSGDCMDecompressNewFile(fn.getPath()));
            if(! f.createNewFile()) {
                throw new TarException("decompress failed: " + f.getPath() + " already exists");
            }

            // create streams
            InputStream in = new MemoryMappedFileInputStream(src, ((RegularFile) fn).getDataOffset());
            OutputStream out = new BufferedOutputStream(new FileOutputStream(f));

            // do compress
            dcm.decompress(in, out);

            // close stream
            in.close();
            out.close();
        } else {
            if(! f.exists()) {
                if(! f.mkdir()) {
                    throw new TarException(
                        "decompress failed: cannot create dir " + fn.getPath());
                }
            }

            for(FileNode ch: ((Menu)fn).getChildren()) {
                doDecompress(f, ch, dcm, src);
            }
        }
    }
}
