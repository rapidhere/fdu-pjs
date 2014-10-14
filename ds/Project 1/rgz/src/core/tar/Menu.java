package core.tar;

import excs.TarException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public void dumpIndex(OutputStream out)
    throws IOException {
        // this is a menu, write 0
        out.write(127);

        // write name
        for(int i = 0;i < 4;i ++) {
            out.write((byte) ((getName().length() >> (i * 8)) & 0xff));
        } 
        out.write(getName().getBytes());

        // dump children
        int childrenCount = children.size();
        for(int i = 0;i < 4;i ++) {
            out.write((byte) ((childrenCount >> (i * 8)) & 0xff));
        }
        for(FileNode ch: children.values()) {
            ch.dumpIndex(out);
        }
    }

    @Override
    public void loadIndex(InputStream in) throws IOException, TarException {
        // read in name length
        int nameLength = 0;
        for(int i = 0;i < 4;i ++) {
            int c = in.read();
            if(c == -1) {
                throw new TarException("load index failed: wrong index format - cannot get name length");
            }
            nameLength |= (c & 0xff) << (i * 8);
        }

        // read name
        char[] name = new char[nameLength];
        for(int i = 0;i < nameLength;i ++) {
            int c = in.read();
            if(c == -1)
                throw new TarException("load index failed: wrong index format - cannot get name");
            name[i] = (char)c;
        }
        setName(String.valueOf(name));

        // load children
        // get children count
        int childrenCount = 0;
        for(int i = 0;i < 4;i ++) {
            int c = in.read();
            if(c == -1) {
                throw new TarException(
                    "load index failed: wrong index format - cannot get children count");
            }
            childrenCount |= (c & 0xff) << (i * 8);
        }

        children.clear();
        for(int i = 0;i < childrenCount;i ++){
            int c = in.read();
            if(c == -1) {
                throw new TarException(
                    "load index failed: wrong index format - cannot load children!");
            }

            FileNode ch;
            if(c == 127)
                ch = new Menu();
            else
                ch = new RegularFile();
            ch.parent = this;
            ch.loadIndex(in);
            children.put(ch.getName(), ch);
        }
    }

    public FileNode findFileNode(String pathString) {
        Path path = Paths.get(pathString).normalize();

        FileNode cur = this;
        for(Path p: path) {
            if(cur instanceof RegularFile)
                return null;
            cur = ((Menu)cur).findChild(p.toString());
            if(cur == null)
                return null;
        }

        return cur;
    }

    public void addFileNode(FileNode fn) throws TarException {
        if(children.containsKey(fn.getName()))
            throw new TarException(fn.getName() + "is already existed");
        children.put(fn.getName(), fn);
    }

    public FileNode[] getChildren() {
        return children.values().toArray(new FileNode[children.size()]);
    }

    public FileNode findChild(String name) {
        if(name.equals(".") || name.equals(""))
            return this;

        return children.get(name);
    }

    public boolean hasChild(String name) {
        return children.containsKey(name);
    }
}
