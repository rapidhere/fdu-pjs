package core.dc;

import excs.BitArrayException;
import excs.DCException;
import javafx.util.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CatchFixedBitsAlgorithm implements CatchAlgorithm <FixedBitsToken> {
    public CatchFixedBitsAlgorithm() {}
    public CatchFixedBitsAlgorithm(byte bitLength) {
        setBitLength(bitLength);
    }

    private byte bitLength;

    public void setBitLength(byte bitLength) {
        this.bitLength = bitLength;
    }

    public byte getBitLength() {
        return bitLength;
    }

    @Override
    public byte[] dump(FixedBitsToken[] tokens)
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

            return new Pair<>(tokens, remainOffset);
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
        return new Pair<>(tokens, offset);
    }

    @Override
    public byte[] merge(FixedBitsToken[] tokens) throws DCException {
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

    @Override
    public void dumpHeader(OutputStream out)
    throws DCException {
        try {
            out.write(getBitLength());
        } catch (IOException e) {
            throw new DCException("dump header failed: " + e.getMessage());
        }
    }

    @Override
    public void loadHeader(InputStream in)
        throws DCException {
        try {
            int b = in.read();
            if(b == -1)
                throw new DCException("Wrong file syntax: cannot load dc ca info!");
            setBitLength((byte) b);
        } catch (IOException e) {
            throw new DCException("load header failed: " + e.getMessage());
        }
    }
}
