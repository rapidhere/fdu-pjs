package core.dc;

import excs.BitArrayException;

import java.util.ArrayList;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 *
 * the effective version of BitArray
 */
public class UnjoinableBitArray {
    private ArrayList<Byte> bits = new ArrayList<>();
    private byte currentByte;
    private byte currentByteLength;

    public UnjoinableBitArray() {
        clear();
    }

    /**
     * add a bit to the very end of bit array
     * @param bit bit to add
     */
    public void addBit(byte bit) {
        currentByte = (byte)(currentByte | (bit << currentByteLength));
        currentByteLength ++;

        if(currentByteLength == 8) {
            bits.add(currentByte);
            currentByteLength = 0;
            currentByte = 0;
        }
    }

    /**
     * return the size of bit array
     * @return the size of bit array
     */
    public int size() {
        return bits.size() * 8 + currentByteLength;
    }

    /**
     * set the bit array to empty
     */
    public void clear() {
        currentByte = 0;
        currentByteLength = 0;
    }

    /**
     * dump bits, can load with load function
     * @return dumped bits
     */
    public byte[] dump() {
        byte[] ret = new byte[4 + bits.size() + 1];

        ret[0] = (byte)(size() & 0xff);
        ret[1] = (byte)((size() >> 8) & 0xff);
        ret[2] = (byte)((size() >> 16) & 0xff);
        ret[3] = (byte)((size() >> 24) & 0xff);

        for(int i = 0;i < bits.size();i ++) {
            ret[i + 4] = bits.get(i);
        }

        ret[ret.length - 1] = currentByte;
        return ret;
    }

    /**
     * load bits into bit array
     * @param b bits
     * @param offset start position
     * @param bLength the length of input bits
     * @return remain offset
     */
    public int load(byte[] b, int offset, int bLength)
        throws BitArrayException {
        clear();
        // calculate length
        if(bLength < 4) {
            throw new BitArrayException("load failed: cannot load length info");
        }
        int length = 0;
        for(int i = 0;i < 4;i ++) {
            length |= (b[i + offset] & 0xff) << (i * 8);
        }

        // calculate bytes length an remain length
        int byteLength = length / 8,
            remainLength = length % 8;
        if(length < 0 || byteLength + 5 > bLength) {
            throw new BitArrayException("load failed: wrong byte length");
        }

        // add bytes
        bits.clear();
        for(int i = 0;i < byteLength;i ++)
            bits.add(b[i + 4 + offset]);

        // set remain
        currentByteLength = (byte)remainLength;
        currentByte = b[offset + 4 + byteLength];

        return offset + 5 + byteLength;
    }

    /**
     * get the bit at index
     * @param index the bit index
     * @return the bit
     */
    public byte get(int index) {
        byte b;
        if(index < bits.size() * 8) {
            b = bits.get(index / 8);
        } else {
            b = currentByte;
        }
        index %= 8;

        return (byte)(((int)b >> index) & 1);
    }

    /**
     * add a bit array into bit array
     * @param ba the bit array to append
     */
    public void addBitArray(UnjoinableBitArray ba) {
        for(byte b: ba.bits) {
            bits.add((byte)((b << currentByteLength) | currentByte));
            currentByte = (byte)((b & 0xff) >> (8 - currentByteLength));
        }

        if(currentByteLength + ba.currentByteLength < 8) {
            currentByte |= (ba.currentByte << currentByteLength);
            currentByteLength += ba.currentByteLength;
        } else {
            bits.add((byte)((ba.currentByte << currentByteLength) | currentByte));
            currentByte = (byte)((ba.currentByte & 0xff) >> (8 - currentByteLength));
            currentByteLength += ba.currentByteLength;
            currentByteLength -= 8;
        }
    }
}