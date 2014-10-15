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
 * Class :
 * Version :
 * Usage :
 */
abstract public class FileNode implements Comparable<FileNode> {
    protected String name;
    protected FileNode parent;

    public String getName() {return name;}
    abstract public void dumpIndex(OutputStream out) throws IOException;
    abstract public void loadIndex(InputStream in) throws IOException, TarException;

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        ArrayList<String> names = new ArrayList<String>();
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
    public int compareTo(FileNode fn) {
        if(fn instanceof Menu && this instanceof RegularFile)
            return 1;
        if(fn instanceof RegularFile && this instanceof Menu)
            return -1;

        return this.getName().compareTo(fn.getName());
    }

    abstract public int getSize() ;
    abstract public int getCompressedSize();
}
