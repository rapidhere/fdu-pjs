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
    int size, compressedSize;

    @Override
    public void dumpIndex(OutputStream out)
    throws IOException {
        // this is a regular file write 1
        out.write(1);

        // write name
        byte[] nameBytes = getName().getBytes();
        for(int i = 0;i < 4;i ++)
            out.write((byte)((nameBytes.length >> (i * 8)) & 0xff));
        out.write(nameBytes);

        // write data offset
        for(int i = 0;i < 4;i ++)
            out.write((byte)((dataOffset >> (i * 8)) & 0xff));

        // dump size
        for(int i = 0;i < 4;i ++)
            out.write((byte)((size >> (i * 8)) & 0xff));

        // dump compressed size
        for(int i = 0;i < 4;i ++)
            out.write((byte)((compressedSize >> (i * 8)) & 0xff));
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
        byte[] name = new byte[nameLength];
        for(int i = 0;i < nameLength;i ++) {
            int c = in.read();
            if(c == -1)
                throw new TarException("load index failed: wrong index format - cannot get name");
            name[i] = (byte)c;
        }
        setName(new String(name, 0, nameLength));

        // read data offset
        dataOffset = 0;
        for(int i = 0;i < 4;i ++) {
            int c = in.read();
            if(c == -1)
                throw new TarException("load index failed: wrong index format - cannot load data offset");
            dataOffset |= (c & 0xff) << (i * 8);
        }

        // read size
        size = 0;
        for(int i = 0;i < 4;i ++) {
            int c = in.read();
            if(c == -1)
                throw new TarException("load index failed: wrong index format - cannot load size");
            size |= (c & 0xff) << (i * 8);
        }

        // read compressed size
        compressedSize = 0;
        for(int i = 0;i < 4;i ++) {
            int c = in.read();
            if(c == -1)
                throw new TarException("load index failed: wrong index format - cannot load compressed size");
            compressedSize |= (c & 0xff) << (i * 8);
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int getCompressedSize() {
        return compressedSize;
    }
}
