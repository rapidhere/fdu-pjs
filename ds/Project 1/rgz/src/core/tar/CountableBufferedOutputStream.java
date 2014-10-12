package core.tar;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class CountableBufferedOutputStream extends BufferedOutputStream {
    int wroteBytes;

    public CountableBufferedOutputStream(OutputStream out) {
        super(out);
        wroteBytes = 0;
    }

    public int getWroteBytes() {
        return wroteBytes;
    }

    @Override
    public void write(int b) throws IOException {
        wroteBytes ++;
        super.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        wroteBytes += b.length;
        super.write(b);
    }

    @Override
    public void write(byte[] b, int offset, int length) throws IOException {
        wroteBytes += length;
        super.write(b, offset, length);
    }
}
