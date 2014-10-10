package core.dc;

import java.util.Vector;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class BitArray {
    private Vector<Byte> bytes = new Vector<Byte>();
    private byte currentByte;
    private byte currentByteLength;

    public BitArray() {
        clear();
    }

    public void addBit(byte bit) {
        if(currentByteLength == 8) {
            bytes.add(currentByte);
            currentByteLength = 1;
            currentByte = bit;
        } else {
            currentByteLength ++;
            currentByte = (byte)(((int)currentByte << 1) & (bit));
        }
    }

    public int size() {
        return bytes.size() + currentByteLength;
    }

    public void clear() {
        currentByte = 0;
        currentByteLength = 0;
    }

    public byte[] dump() {
        byte[] ret;
        if(currentByteLength == 0)
            ret = new byte[bytes.size() + 4];
        else
            ret = new byte[bytes.size() + 5];

        int length = size();
        for(int i = 0;i < 4;i ++) {
            ret[i] = (byte)(length & 0xff);
            length >>= 8;
        }

        for(int i = 4;i < bytes.size();i ++)
            ret[i] = bytes.get(i - 4);

        if(currentByteLength != 0)
            ret[bytes.size() - 1] = currentByte;

        return ret;
    }

    public void load(byte[] b) {
        // calculate length
        int length = 0;
        for(int i = 0;i < 4;i ++) {
            length |= ((int)b[i]) << i * 8;
        }

        // calculate bytes length an remain length
        int byteLength = length / 8,
            remainLength = length % 8;

        // add bytes
        bytes.clear();
        for(int i = 0;i < byteLength;i ++)
            bytes.add(b[i + 4]);

        // set remain
        currentByteLength = 0;
        currentByte = 0;
        if(remainLength != 0) {
            currentByteLength = (byte)remainLength;
            currentByte = b[b.length - 1];
        }
    }
}
