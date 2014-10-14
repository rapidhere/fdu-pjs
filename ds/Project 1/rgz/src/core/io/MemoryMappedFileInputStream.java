package core.io;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class MemoryMappedFileInputStream extends InputStream {
    private final static int memoryMappedBufferSize = 64 * 1024 * 1024;

    protected MappedByteBuffer mBuffer;
    protected int length, offset;
    protected FileChannel fc;
    protected long filePosition;

    public MemoryMappedFileInputStream(File file, long position)
    throws IOException {
        fc = new FileInputStream(file).getChannel();
        filePosition = position;
    }

    public void fill()
    throws IOException {
        length = (int)Math.min(memoryMappedBufferSize, fc.size() - filePosition);

        if(length == 0) {
            offset = 0;
            return ;
        }

        // create mappedBuffer
        mBuffer = fc.map(FileChannel.MapMode.READ_ONLY, filePosition, length);

        // reset offset
        offset = 0;
        filePosition += length;
    }

    @Override
    public int read()
    throws IOException {
        if(offset >= length) {
            fill();
            if(offset >= length)
                return -1;
        }

        offset ++;
        return mBuffer.get(offset - 1) & 0xff;
    }

    @Override
    public void close()
    throws IOException {
        fc.close();
    }
}
