package core.tar;

import excs.TarException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
abstract public class FileNode {
    protected String name;

    public String getName() {return name;}
    abstract public void dumpIndex(OutputStream out) throws IOException;
    abstract public void loadIndex(InputStream in) throws IOException, TarException;

    public void setName(String name) {
        this.name = name;
    }
}
