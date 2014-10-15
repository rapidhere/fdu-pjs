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
abstract public class FileNode {
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
}