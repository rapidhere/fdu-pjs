package core.dc;

import ui.Config;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
abstract public class DCFactory <T extends Token> {
    abstract public DCHuffmanAlgorithm<T> getHuffmanDC();
    abstract public DCRaw<T> getRawDC();
    abstract public CatchAlgorithm<T> getCA();
    abstract public BlockDCM<T> getBlockDCM();

    public DCM<T> getDCM(byte dcmId, byte algId) {
        DCAlgorithm<T> alg = null;

        switch (algId) {
            case Config.DC_RAW: alg = getRawDC(); break;
            case Config.DC_HUFFMAN: alg = getHuffmanDC(); break;
        }

        assert alg != null;

        DCM<T> dcm = null;
        switch (dcmId) {
            case Config.DCM_BLOCK: dcm =  getBlockDCM(); break;
        }

        assert dcm != null;

        dcm.setDCAlgorithm(alg);
        dcm.setCatchAlgorithm(getCA());

        return dcm;
    }
}

abstract class Token<T> implements Comparable<Token<T>> {
    private T token;

    public abstract int hashCode();

    @Override
    public boolean equals(Object o) {
        return o instanceof Token && this.hashCode() == o.hashCode();
    }

    public T getToken() {
        return token;
    }

    public void setToken(T o) {
        token = o;
    }
}
