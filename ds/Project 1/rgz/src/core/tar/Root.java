package core.tar;

import core.dc.DC;
import excs.DCException;
import excs.TarException;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class Root extends Menu {
    ArrayList<RegularFile> regularFiles = new ArrayList<RegularFile>();

    @Override
    public void dumpIndex(OutputStream out) {
        super.dumpIndex(out);

        // TODO: dump compressed data index
    }

    @Override
    public void loadIndex(InputStream in) {
        super.loadIndex(in);

        // TODO: load compressed data index
    }

    public InputStream getIndexInputStream(String srcFile){
        return null;
    }

    private void addNewFileNode(String srcFileName)
    throws TarException {
        Path srcPath = Paths.get(srcFileName);
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
            }

            next.setName(cName);
            ((Menu)cur).addFileNode(next);
            cur = next;
        }

        if(cur instanceof RegularFile) { // this is a regular file
            regularFiles.add((RegularFile)cur);
            ((RegularFile) cur).setPath(srcFileName);
        } else { // this is a directory
            discoverDirectory((Menu)cur);
        }
    }

    public void discoverDirectory(Menu m) {

    }

    public void compress(String[] sourceFiles, String outputFile, DC dc)
        throws TarException, DCException {
        // clear up
        regularFiles.clear();

        // create Output stream
        OutputStream out;
        try {
            out = new CountableBufferedOutputStream(new FileOutputStream(outputFile));
        } catch (FileNotFoundException e) {
            throw new TarException(e.getMessage());
        }

        // add sourceFiles
        for(String srcFileName: sourceFiles) {
            File srcFile = new File(srcFileName);
            // no such file
            if(!srcFile.exists()) {
                throw new TarException("cannot found source file or directory " + srcFileName);
            }

            // add new file node
            addNewFileNode(srcFileName);
        }

        // index has build up
        // then compress files
        for(RegularFile f: regularFiles) {
            InputStream in = null;
            try {
                in = new BufferedInputStream(new FileInputStream(f.getPath()));
            } catch (FileNotFoundException e) {
                // never reach here
                assert(false);
            }

            dc.compress(in, out);
            f.setDataOffset(((CountableBufferedOutputStream)out).getWroteBytes());
        }

        // dump index to the very end of the file
        dumpIndex(out);

        // close output stream
        try {
            out.close();
        } catch (IOException e) {
            throw new TarException(e.getMessage());
        }
    }

    public void decompress(String sourceFile, String[] targetFiles, DC dc) {
        // load index info
        loadIndex(getIndexInputStream(sourceFile));
    }

    public void list(String sourceFile, String[] paths) {
        // load index info
        loadIndex(getIndexInputStream(sourceFile));
    }
}
