package core.tar;

import core.dc.DCM;
import core.io.CountableBufferedOutputStream;
import core.io.MemoryMappedFileInputStream;
import excs.DCException;
import excs.TarException;
import excs.UnknownDCM;
import excs.UnknownDCMId;
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
 * Class :
 * Version :
 * Usage :
 */
public class Root extends Menu {
    Map<RegularFile, String> regularFilePathsMap = new HashMap<RegularFile, String>();

    public Root() {
        setName(".");
        parent = null;
    }

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

        if(in.available() != 0)
            throw new TarException("cannot load index: wrong index format - redundant bytes");
    }

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
            loadIndex(new ByteArrayInputStream(b.array()));
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

    public void addSource(String srcFileName)
    throws TarException {
        Path srcPath = Paths.get(srcFileName);
        if(! srcPath.toFile().exists())
            throw new TarException("cannot found source file or directory " + srcFileName);

        FileNode cur = this;

        // normalize path
        srcPath = srcPath.normalize();

        // found the target node in the index tree
        for(int i = 0;i < srcPath.getNameCount();i ++) {
            String cName = srcPath.getName(i).toString();

            // check name
            if(cName.equals("..")) {
                throw new TarException("Don't use '..' in the path name!");
            }

            FileNode next;
            if(((Menu)cur).hasChild(cName)) {
                next = ((Menu)cur).findChild(cName);
            } else {
                if(i == srcPath.getNameCount() - 1) {
                    if(srcPath.toFile().isFile()) {
                        next = new RegularFile();
                    } else {
                        next = new Menu();
                    }
                } else {
                    next = new Menu();
                }
                next.setName(cName);
                next.parent = cur;
                ((Menu)cur).addFileNode(next);
            }

            cur = next;
        }

        if(cur instanceof RegularFile) { // this is a regular file
            regularFilePathsMap.put((RegularFile) cur, srcFileName);
        } else { // this is a directory
            discoverDirectory((Menu)cur, srcPath);
        }
    }

    public void discoverDirectory(Menu m, Path p)
    throws TarException {
        File dir = p.toFile();

        for(String f: dir.list()) {
            if(m.hasChild(f))
                continue;

            File cf = new File(dir, f);

            FileNode t;
            if(cf.isFile()) { // regular File
                t = new RegularFile();
                t.setName(f);
                t.parent = m;
                regularFilePathsMap.put((RegularFile)t, cf.getPath());
            } else {
                t = new Menu();
                t.setName(f);
                t.parent = m;
                discoverDirectory((Menu)t, Paths.get(cf.getPath()));
            }
            m.addFileNode(t);
        }
    }

    public void compress(String outputFile, DCM dcm)
    throws TarException, DCException {
        // create Output stream
        OutputStream out;
        try {
            out = new CountableBufferedOutputStream(new FileOutputStream(outputFile));
        } catch (FileNotFoundException e) {
            throw new TarException(e.getMessage());
        }

        // dump dcm info
        try {
            out.write(Config.getDCMId(dcm));
        } catch (UnknownDCM e) {
            throw new TarException("compress failed: " + e.getMessage());
        } catch (IOException e) {
            throw new TarException("compress failed: " + e.getMessage());
        }

        // index has build up
        // then compress files
        for(RegularFile f: regularFilePathsMap.keySet()) {
            InputStream in = null;
            try {
                in = new BufferedInputStream(new FileInputStream(regularFilePathsMap.get(f)));
            } catch (FileNotFoundException e) {
                // never reach here
                assert(false);
            }

            f.setDataOffset(((CountableBufferedOutputStream)out).getWroteBytes());
            dcm.compress(in, out);
        }

        // close output stream
        try {
            // dump index to the very end of the file
            dumpIndex(out);
            out.close();
        } catch (IOException e) {
            throw new TarException(e.getMessage());
        }
    }

    public void decompress(String rootDirString, FileNode[] fileNodes, String srcFile)
    throws TarException, DCException {
        // load dcm
        DCM dcm;
        try {
            byte dcmId = (byte)(new FileInputStream(srcFile).read());
            if(dcmId == -1)
               throw new TarException("decompress failed: cannot read out dcm id");

            dcm = Config.getDCMbyId(dcmId);
        } catch (IOException e) {
            throw new TarException("decompress failed: " + e.getMessage());
        } catch (UnknownDCMId e) {
            throw new TarException("decompress failed: " + e.getMessage());
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
        for(FileNode fn: fileNodes) {
            Path iPath = Paths.get(fn.getPath());
            File cur = rootDir;

            for(int i = 0;i < iPath.getNameCount() - 1;i ++) {
                Path p = iPath.getName(i);

                cur = new File(rootDir, p.toString());
                if(! cur.exists()) {
                    if(! cur.mkdir())
                        throw new TarException(
                           "decompress failed: cannot create dir " + fn.getPath());
                }
                rootDir = cur;
            }
            try {
                doDecompress(cur, fn, dcm, src);
            } catch (IOException e) {
                e.printStackTrace();
                throw new TarException("decompress failed: " + e.getMessage());
            }
        }
    }

    static void doDecompress(File m, FileNode fn, DCM dcm, File src)
    throws IOException, DCException, TarException {
        File f = new File(m, fn.getName());

        if(fn instanceof RegularFile) {
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
