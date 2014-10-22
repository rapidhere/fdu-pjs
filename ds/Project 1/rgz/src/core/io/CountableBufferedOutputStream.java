package core.io;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class CountableBufferedOutputStream extends BufferedOutputStream {
    int wroteBytes;
    ReadWriteLock writeLock = new ReentrantReadWriteLock();

    public CountableBufferedOutputStream(OutputStream out) {
        super(out);
        wroteBytes = 0;
    }

    public int getWroteBytes() {
        return wroteBytes;
    }
    public void clearCounter() {wroteBytes = 0;}

    @Override
    public void write(int b) throws IOException {
        wroteBytes ++;
        writeLock.writeLock().lock();
        super.write(b);
        writeLock.writeLock().unlock();
    }

    @Override
    public void write(@SuppressWarnings("NullableProblems") byte[] b) throws IOException {
        writeLock.writeLock().lock();
        super.write(b);
        writeLock.writeLock().unlock();
    }

    @Override
    public void write(@SuppressWarnings("NullableProblems") byte[] b, int offset, int length) throws IOException {
        wroteBytes += length;
        writeLock.writeLock().lock();
        super.write(b, offset, length);
        writeLock.writeLock().unlock();
    }
}
