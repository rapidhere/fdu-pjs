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

public class RegularFile extends FileNode {
    public int getDataOffset() {
        return dataOffset;
    }

    public void setDataOffset(int dataOffset) {
        this.dataOffset = dataOffset;
    }

    private int dataOffset;

    @Override
    public void dumpIndex(OutputStream out)
    throws IOException {
        // this is a regular file write 1
        out.write(1);

        // write name
        for(int i = 0;i < 4;i ++)
            out.write((byte)((getName().length() >> (i * 8)) & 0xff));
        out.write(getName().getBytes());

        // write data offset
        for(int i = 0;i < 4;i ++)
            out.write((byte)((dataOffset >> (i * 8)) & 0xff));
    }

    @Override
    public void loadIndex(InputStream in) throws TarException, IOException {
        // read in name length
        int nameLength = 0;
        for(int i = 0;i < 4;i ++) {
            int c = in.read();
            if(c == -1)
                throw new TarException("load index failed: wrong index format - cannot get name length");
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

        // read data offset
        dataOffset = 0;
        for(int i = 0;i < 4;i ++) {
            int c = in.read();
            if(c == -1)
                throw new TarException("load index failed: wrong index format - cannot load data offset");
            dataOffset |= (c & 0xff) << (i * 8);
        }
    }
}
