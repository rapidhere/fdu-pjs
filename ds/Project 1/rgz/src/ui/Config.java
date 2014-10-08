package ui;

import core.dc.DCAlgorithm;
import core.dc.DCHuffmanAlgorithm;
import excs.UnknownAlgorithmId;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class Config {
    public static final int ALG_HUFFMAN = 0;

    private int algId;

    public void setAlgorithm(int aid) {
        algId = aid;
    }

    public DCAlgorithm getAlgorithm()
    throws UnknownAlgorithmId {
        switch (algId) {
            case ALG_HUFFMAN: return new DCHuffmanAlgorithm();
            default: throw new UnknownAlgorithmId(algId);
        }
    }
}
