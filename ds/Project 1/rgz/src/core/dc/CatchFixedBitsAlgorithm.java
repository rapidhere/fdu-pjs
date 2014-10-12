package core.dc;

import com.sun.istack.internal.NotNull;
import excs.BitArrayException;
import excs.DCException;
import javafx.util.Pair;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */

class FixedBitsToken extends Token{
    private final int bitLength;

    public FixedBitsToken(int bitLength) {
        this.bitLength = bitLength;
    }

    @Override
    public int hashCode() {
        int ret = 0;
        BitArray b = (BitArray)getToken();
        for(int i = 0;i < b.size();i ++) {
            ret <<= 1;
            ret |= b.get(i);
        }

        return ret;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        BitArray b1 = (BitArray)getToken(),
            b2 = (BitArray)((Token)o).getToken();
        assert b1.size() == b2.size();
        assert b2.size() == bitLength;

        for(int i = 0;i < b1.size();i ++) {
            int cb1 = b1.get(i),
                cb2 = b2.get(i);
            if(cb1 < cb2)
                return -1;
            if(cb1 > cb2)
                return 1;
        }

        return 0;
    }
}

public class CatchFixedBitsAlgorithm implements CatchAlgorithm <FixedBitsToken> {
    private byte bitLength;

    public void setBitLength(byte bitLength) {
        this.bitLength = bitLength;
    }

    public byte getBitLength() {
        return bitLength;
    }

    @Override
    public byte[] dump(Token[] tokens)
    throws DCException {
        BitArray ret = new BitArray();

        for (Token token : tokens)
            ret.appendBitArray((BitArray) token.getToken());

        return ret.dump();
    }

    @Override
    public Pair<FixedBitsToken[], Integer> load(byte[] bytes, int offset, int length)
    throws DCException {
        try {
            BitArray ba = new BitArray();
            int remainOffset = ba.load(bytes, offset, length);

            if(ba.size() % bitLength != 0) {
                throw new DCException("load failed: Wrong bit size!");
            }

            FixedBitsToken[] tokens = new FixedBitsToken[ba.size() / bitLength];
            for(int i = 0;i < tokens.length;i ++) {
                BitArray cba = new BitArray();
                for(int j = 0;j < bitLength;j ++) {
                    cba.addBit(ba.get(i * bitLength + j));
                }

                tokens[i] = new FixedBitsToken(bitLength);
                tokens[i].setToken(cba);
            }

            return new Pair<FixedBitsToken[], Integer>(tokens, remainOffset);
        } catch (BitArrayException e) {
            throw new DCException(e.getMessage());
        }
    }

    static int gcd(int a, int b) {
        if(a < b)
            return gcd(b, a);

        int r = 1;
        while(r != 0) {
            r = a % b;
            a = b;
            b = r;
        }
        return a;
    }

    @Override
    public Pair<FixedBitsToken[], Integer> parse(byte[] bytes, int offset, int length)
    throws DCException {
        int blkSize = bitLength * 8 / gcd(bitLength, 8);
        int tokenSize = length * 8 / blkSize * (blkSize / bitLength);
        FixedBitsToken[] tokens = new FixedBitsToken[tokenSize];
        int maxOffset = offset + length;

        byte remainBit = 0, remainLength = 0;
        for(int i = 0; i < tokens.length;i ++) {
            tokens[i] = new FixedBitsToken(bitLength);
            BitArray ba = new BitArray();

            int j = 0;
            for(;remainLength > 0 && j < bitLength;j ++) {
                ba.addBit((byte)(remainBit & 1));
                remainBit >>= 1;
                remainLength --;
            }

            if(remainLength == 0) {
                if(offset == maxOffset) {
                    tokens[i].setToken(ba);
                    break;
                }
                remainBit = bytes[offset ++];
                remainLength = 8;
            }

            for(;j < bitLength;j ++) {
                ba.addBit((byte)(remainBit & 1));
                remainBit >>= 1;
                remainLength --;
            }
            tokens[i].setToken(ba);
        }

        // trick: offset - 1
        return new Pair<FixedBitsToken[], Integer>(tokens, offset);
    }

    @Override
    public byte[] merge(Token[] tokens) throws DCException {
        BitArray ba = new BitArray();
        for(Token t: tokens) {
            BitArray newBa = new BitArray(),
                cba = (BitArray)t.getToken();

            for(int i = 0;i < cba.size();i ++)
                newBa.addBit(cba.get(i));

            ba.appendBitArray(newBa);
        }
        if(ba.size() % 8 != 0)
            throw new DCException("merge failed: Wrong bit size");
        byte[] ret = new byte[ba.size() / 8];
        System.arraycopy(ba.dump(), 4, ret, 0, ret.length);
        return ret;
    }
}
