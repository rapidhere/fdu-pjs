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
        currentByte = (byte)(((int)currentByte) | ((int)bit << currentByteLength));
        currentByteLength ++;

        if(currentByteLength == 8) {
            bytes.add(currentByte);
            currentByteLength = 0;
            currentByte = 0;
        }
    }

    public int size() {
        return bytes.size() * 8 + currentByteLength;
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

        for(int i = 0;i < bytes.size();i ++)
            ret[i + 4] = bytes.get(i);

        if(currentByteLength != 0)
            ret[ret.length - 1] = currentByte;

        return ret;
    }

    public void load(byte[] b, int offset) {
        // calculate length
        int length = 0;
        for(int i = 0;i < 4;i ++) {
            length |= (b[i + offset] & 0xff) << (i * 8);
        }

        // calculate bytes length an remain length
        int byteLength = length / 8,
            remainLength = length % 8;

        // add bytes
        bytes.clear();
        for(int i = 0;i < byteLength;i ++)
            bytes.add(b[i + 4 + offset]);

        // set remain
        currentByteLength = 0;
        currentByte = 0;
        if(remainLength != 0) {
            currentByteLength = (byte)remainLength;
            currentByte = b[offset + 4 + byteLength];
        }
    }

    public byte get(int index) {
        if(index >= size())
            throw new IndexOutOfBoundsException();

        byte b;
        if(index < bytes.size() * 8) {
            b = bytes.get(index / 8);
        } else {
            b = currentByte;
        }
        index %= 8;

        return (byte)(((int)b >> index) & 1);
    }
}
