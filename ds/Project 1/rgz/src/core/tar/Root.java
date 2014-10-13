package core.tar;

import core.dc.DC;
import core.io.CountableBufferedOutputStream;
import excs.DCException;
import excs.TarException;

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

    public Root() {setName(".");}

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
        if(b != 0) { // root must be a menu!
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

    private void addSource(String srcFileName)
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
                next = ((Menu)cur).findFileNode(cName);
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
                ((Menu)cur).addFileNode(next);
            }

            cur = next;
        }

        if(cur instanceof RegularFile) { // this is a regular file
            regularFilePathsMap.put((RegularFile) cur, srcFileName);
        } else { // this is a directory
            discoverDirectory((Menu)cur);
        }
    }

    public void discoverDirectory(Menu m) {
        // TODO
    }

    public void compress(String outputFile, String srcFile, DC dc)
    throws TarException, DCException {
        // create Output stream
        OutputStream out;
        try {
            out = new CountableBufferedOutputStream(new FileOutputStream(outputFile));
        } catch (FileNotFoundException e) {
            throw new TarException(e.getMessage());
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

            dc.compress(in, out);
            f.setDataOffset(((CountableBufferedOutputStream)out).getWroteBytes());
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

    public void decompress(FileNode[] fileNodes, String srcFile, DC dc)
    throws TarException, DCException {
        // TODO
    }
}
