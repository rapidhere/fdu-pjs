package core.dc;

import excs.BitArrayException;

import java.util.ArrayList;
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
    private BitArray nextBitArray;

    public BitArray() {
        clear();
    }

    /**
     * add a bit to the very end of bit array
     * @param bit bit to add
     */
    public void addBit(byte bit) {
        if(nextBitArray != null)
            nextBitArray.addBit(bit);

        currentByte = (byte)(((int)currentByte) | ((int)bit << currentByteLength));
        currentByteLength ++;

        if(currentByteLength == 8) {
            bytes.add(currentByte);
            currentByteLength = 0;
            currentByte = 0;
        }
    }

    /**
     * return the size of bit array
     * @return the size of bit array
     */
    public int size() {
        int curSize = bytes.size() * 8 + currentByteLength;
        if(nextBitArray != null)
            return curSize + nextBitArray.size();
        return curSize;
    }

    /**
     * set the bit array to empty
     */
    public void clear() {
        currentByte = 0;
        currentByteLength = 0;
        nextBitArray = null;
    }

    /**
     * dump bytes, can load with load function
     * @return dumped bytes
     */
    public byte[] dump() {
        int totSize = size();
        ArrayList<Byte> ret = new ArrayList<Byte>();

        // write length
        for(int i = 0;i < 4;i ++) {
            ret.add((byte)(totSize & 0xff));
            totSize >>= 8;
        }

        // dump bytes
        byte remainByte = 0, remainLength = 0;

        BitArray ba = this;
        while(ba != null) {
            for(int i = 0;i < ba.bytes.size();i ++) {
                byte cb = ba.bytes.get(i);
                ret.add((byte) (remainByte | (cb << remainLength)));
                remainByte = (byte)((cb & 0xff) >> (8 - remainLength));
            }

            if(remainLength + ba.currentByteLength < 8) {
                remainByte = (byte)((ba.currentByte << remainLength) | remainByte);
                remainLength += ba.currentByteLength;
            } else {
                byte cb = ba.currentByte;
                ret.add((byte)(remainByte | (cb << remainLength)));
                remainByte = (byte)((cb & 0xff) >> (8 - remainLength));
                remainLength = (byte)(remainLength + ba.currentByteLength - 8);
            }

            ba = ba.nextBitArray;
        } 
        if(remainLength != 0)
            ret.add(remainByte);

        byte[] r = new byte[ret.size()];
        for(int i = 0;i < ret.size();i ++)
            r[i] = ret.get(i);

        return r;
    }

    /**
     * load bytes into bit array
     * @param b bytes
     * @param offset start position
     * @param bLength the length of input bytes
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
        if(length < 0 || byteLength + (remainLength > 0 ? 1 : 0) + 4 > bLength) {
            throw new BitArrayException("load failed: wrong byte length");
        }

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

        // next ba is none
        nextBitArray = null;

        return offset + 4 + byteLength + (remainLength > 0 ? 1 : 0);
    }

    /**
     * get the bit at index
     * @param index the bit index
     * @return the bit
     */
    public byte get(int index) {
        if(index >= bytes.size() * 8 + currentByteLength) {
            if(nextBitArray != null)
                return nextBitArray.get(index - bytes.size() * 8 - currentByteLength);
            else
                throw new IndexOutOfBoundsException();
        }

        byte b;
        if(index < bytes.size() * 8) {
            b = bytes.get(index / 8);
        } else {
            b = currentByte;
        }
        index %= 8;

        return (byte)(((int)b >> index) & 1);
    }

    /**
     * append a bit array into this bit array
     * @param ba the bit array to append
     */
    public void appendBitArray(BitArray ba) {
        BitArray p = this;
        while(true) {
            if(p.nextBitArray == null) {
                p.nextBitArray = ba;
                break;
            }

            p = p.nextBitArray;
        }
    }
}
