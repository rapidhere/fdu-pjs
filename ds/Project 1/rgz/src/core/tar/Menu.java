package core.tar;

import excs.TarException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class Menu extends FileNode {
    protected HashMap<String, FileNode> children = new HashMap<String, FileNode>();

    @Override
    public void dumpIndex(OutputStream out) {
    }

    @Override
    public void loadIndex(InputStream in) {
    }

    public void addFileNode(FileNode fn) throws TarException {
        if(children.containsKey(fn.getName()))
            throw new TarException(fn.getName() + "is already existed");
    }

    public FileNode[] getChildren() {
        return children.values().toArray(new FileNode[children.size()]);
    }

    public FileNode findFileNode(String name) {
        return null;
    }

    public boolean hasChild(String name) {
        return children.containsKey(name);
    }
}
