package core.io;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * The output stream use memory-mapped file
 */
public class MemoryMappedFileInputStream extends InputStream {
    private final static int memoryMappedBufferSize = 64 * 1024 * 1024;

    protected MappedByteBuffer mBuffer;
    protected int length, offset;
    protected long fileSize;
    protected FileChannel fc;
    protected long filePosition;

    /**
     * create a memory-mapped input stream
     * @param file the file
     * @param position the position of file
     * @throws IOException
     */
    public MemoryMappedFileInputStream(File file, long position)
    throws IOException {
        fc = new FileInputStream(file).getChannel();
        filePosition = position;
        fileSize = fc.size();
    }

    private void fill()
    throws IOException {
        length = (int)Math.min(memoryMappedBufferSize, fileSize - filePosition);

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

    @Override
    public int available() {
        return (int)(fileSize - filePosition + length - offset);
    }
}
