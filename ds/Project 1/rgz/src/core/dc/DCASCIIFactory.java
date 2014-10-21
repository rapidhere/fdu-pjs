package core.dc;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */

public class DCASCIIFactory extends DCFactory<ASCIIToken> {
    DCHuffmanAlgorithm<ASCIIToken> dcHuffman = new DCHuffmanAlgorithm<>();
    DCRaw<ASCIIToken> dcRaw = new DCRaw<>();
    BlockDCM<ASCIIToken> blockDCM = new BlockDCM<>();
    CatchASCIIAlgorithm ca = new CatchASCIIAlgorithm();

    @Override
    public DCHuffmanAlgorithm<ASCIIToken> getHuffmanDC() {
        return dcHuffman;
    }

    @Override
    public DCRaw<ASCIIToken> getRawDC() {
        return dcRaw;
    }

    @Override
    public CatchASCIIAlgorithm getCA() {
        return ca;
    }

    @Override
    public BlockDCM<ASCIIToken> getBlockDCM() {
        return blockDCM;
    }
}

class ASCIIToken extends Token <Byte> {
    @Override
    public int hashCode() {
        return getToken().intValue();
    }

    @Override
    public int compareTo(@SuppressWarnings("NullableProblems") Token<Byte> o) {
        if(o.hashCode() < hashCode())
            return -1;
        else if(o.hashCode() > hashCode())
            return 1;
        return 0;
    }

    @Override
    public String toString() {
        return "" + (char)getToken().shortValue();
    }
}
