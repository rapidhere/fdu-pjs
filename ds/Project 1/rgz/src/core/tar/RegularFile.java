package core.tar;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */

public class RegularFile extends FileNode {
    public int getDataOffset() {
        return dataOffset;
    }

    public void setDataOffset(int dataOffset) {
        this.dataOffset = dataOffset;
    }

    private int dataOffset;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private String path;

    @Override
    public void dumpIndex(OutputStream out) {
    }

    @Override
    public void loadIndex(InputStream in) {
    }
}
