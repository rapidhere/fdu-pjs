package core.dc;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * The factory used by fixed bytes algorithm
 */

public class DCFixedBytesFactory extends DCFactory<FixedBytesToken> {
    DCHuffmanAlgorithm<FixedBytesToken> dcHuffman = new DCHuffmanAlgorithm<>();
    DCRaw<FixedBytesToken> dcRaw = new DCRaw<>();
    BlockDCM<FixedBytesToken> blockDCM = new BlockDCM<>();
    CatchFixedBytesAlgorithm ca = new CatchFixedBytesAlgorithm();
    SequentialBlockDCM<FixedBytesToken> sequentialBlockDCM = new SequentialBlockDCM<>();

    public DCFixedBytesFactory() {
    }

    @Override
    public DCHuffmanAlgorithm<FixedBytesToken> getHuffmanDC() {
        return dcHuffman;
    }

    @Override
    public DCRaw<FixedBytesToken> getRawDC() {
        return dcRaw;
    }

    @Override
    public CatchFixedBytesAlgorithm getCA() {
        return ca;
    }

    @Override
    public BlockDCM<FixedBytesToken> getBlockDCM() {
        return blockDCM;
    }

    @Override
    public SequentialBlockDCM<FixedBytesToken> getSequentialDCM() {
        return sequentialBlockDCM;
    }
}

/**
 * the token used by fixed-bytes algorithm
 */
class FixedBytesToken extends Token<byte[]>{
    private byte byteLength;
    public FixedBytesToken(byte byteLength) {
        this.byteLength = byteLength;
    }

    @Override
    public int hashCode() {
        byte[] b = getToken();
        long ret = 0;
        for (byte aB : b) {
            ret *= 10007;
            ret += aB;
            ret %= 1000000000 + 7;
        }

        return (int)ret;
    }

    @Override
    public int compareTo(@SuppressWarnings("NullableProblems") Token<byte[]> o) {
        byte[] b1 = getToken(),
            b2 = getToken();

        assert b1.length == b2.length;
        assert b1.length == byteLength;

        for(int i = 0;i < byteLength;i ++) {
            if(b1[i] < b2[i])
                return -1;
            if(b1[i] > b2[i])
                return 1;
        }
        return 0;
    }
}

