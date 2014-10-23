package core.dc;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */

public class DCFixedBitsFactory extends DCFactory<FixedBitsToken> {
    DCHuffmanAlgorithm<FixedBitsToken> dcHuffman = new DCHuffmanAlgorithm<>();
    DCRaw<FixedBitsToken> dcRaw = new DCRaw<>();
    BlockDCM<FixedBitsToken> blockDCM = new BlockDCM<>();
    CatchFixedBitsAlgorithm ca = new CatchFixedBitsAlgorithm();
    SequentialBlockDCM<FixedBitsToken> sequentialBlockDCM = new SequentialBlockDCM<>();

    @Override
    public DCHuffmanAlgorithm<FixedBitsToken> getHuffmanDC() {
        return dcHuffman;
    }

    @Override
    public DCRaw<FixedBitsToken> getRawDC() {
        return dcRaw;
    }

    @Override
    public CatchFixedBitsAlgorithm getCA() {
        return ca;
    }

    @Override
    public BlockDCM<FixedBitsToken> getBlockDCM() {
        return blockDCM;
    }

    @Override
    public SequentialBlockDCM<FixedBitsToken> getSequentialDCM() {
        return sequentialBlockDCM;
    }
}
class FixedBitsToken extends Token <BitArray> {
    private final int bitLength;

    public FixedBitsToken(int bitLength) {
        this.bitLength = bitLength;
    }

    @Override
    public int hashCode() {
        int ret = 0;
        BitArray b = getToken();
        for(int i = 0;i < b.size();i ++) {
            ret <<= 1;
            ret |= b.get(i);
        }

        return ret;
    }

    @Override
    public int compareTo(@SuppressWarnings("NullableProblems") Token<BitArray> o) {
        BitArray b1 = getToken(),
            b2 = o.getToken();
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

