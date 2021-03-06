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
 * A Menu is the container in the file node
 */
public class Menu extends FileNode {
    protected HashMap<String, FileNode> children = new HashMap<>();

    @Override
    public void dumpIndex(OutputStream out)
    throws IOException {
        // this is a menu, write 0
        out.write(127);

        byte[] nameBytes = getName().getBytes();
        // write name
        for(int i = 0;i < 4;i ++) {
            out.write((byte) ((nameBytes.length >> (i * 8)) & 0xff));
        } 
        out.write(nameBytes);

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
        byte[] name = new byte[nameLength];
        for(int i = 0;i < nameLength;i ++) {
            int c = in.read();
            if(c == -1)
                throw new TarException("load index failed: wrong index format - cannot get name");
            name[i] = (byte)c;
        }
        setName(new String(name, 0, nameLength));

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

    int size = -1, compressedSize = -1;
    @Override
    public int getSize() {
        if(size == -1) {
            size = 0;
            for(FileNode fn: getChildren())
                size += fn.getSize();
        }
        return size;
    }

    @Override
    public int getCompressedSize() {
        if(compressedSize == -1) {
            compressedSize = 0;
            for(FileNode fn: getChildren())
                compressedSize += fn.getCompressedSize();
        }
        return compressedSize;
    }

    /**
     * find a file node in current menu
     * @param pathString the path string
     * @return the file node if found or null
     */
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

    /**
     * add the file node as a child into menu
     * @param fn file node
     * @throws TarException
     */
    public void addFileNode(FileNode fn) throws TarException {
        if(children.containsKey(fn.getName()))
            throw new TarException(fn.getName() + "is already existed");
        children.put(fn.getName(), fn);
        size = compressedSize = -1;
    }

    /**
     * get the children array
     * @return array of file nodes
     */
    public FileNode[] getChildren() {
        return children.values().toArray(new FileNode[children.size()]);
    }

    /**
     * find a child by name
     * @param name the name
     * @return the child or null
     */
    public FileNode findChild(String name) {
        if(name.equals(".") || name.equals(""))
            return this;

        return children.get(name);
    }

    /**
     * test if menu has this child
     * @param name the name of child
     * @return true if has
     */
    public boolean hasChild(String name) {
        return children.containsKey(name);
    }

    @Override
    public void remarkSize(){
        compressedSize = size = -1;
        for(FileNode ch: getChildren())
            ch.remarkSize();
    }
}
