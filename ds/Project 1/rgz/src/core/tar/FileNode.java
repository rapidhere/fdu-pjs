package core.tar;

import excs.TarException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * The abstract file node in the index
 */
abstract public class FileNode implements Comparable<FileNode> {
    protected String name;
    protected FileNode parent;

    /**
     * get the name of file node
     * @return the name
     */
    public String getName() {return name;}

    /**
     * dump index into output stream of current node
     * @param out output stream
     * @throws IOException
     */
    abstract public void dumpIndex(OutputStream out) throws IOException;

    /**
     * load index from input stream to current node
     * @param in input stream
     * @throws IOException
     * @throws TarException
     */
    abstract public void loadIndex(InputStream in) throws IOException, TarException;

    /**
     * set the name of file node
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get the path from current node to root
     * @return the path
     */
    public String getPath() {
        ArrayList<String> names = new ArrayList<>();
        FileNode cur = this;
        while(cur != null) {
            names.add(cur.getName());
            cur = cur.parent;
        }

        StringBuilder ret = new StringBuilder();
        for(int i = names.size() - 1;i >= 0;i --) {
            ret.append(names.get(i));
            if(i != 0)
                ret.append(File.separatorChar);
        }

        return ret.toString();
    }

    @Override
    public int compareTo(@SuppressWarnings("NullableProblems") FileNode fn) {
        if(fn instanceof Menu && this instanceof RegularFile)
            return 1;
        if(fn instanceof RegularFile && this instanceof Menu)
            return -1;

        return this.getName().compareTo(fn.getName());
    }

    /**
     * get the size of file node
     * @return the size
     */
    abstract public int getSize() ;

    /**
     * get compressed size of file node
     * @return the compressed node
     */
    abstract public int getCompressedSize();

    /**
     * remark the size, so the fileNode will update the size
     */
    public void remarkSize() {}
}
