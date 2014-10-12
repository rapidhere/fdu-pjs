package ui;

import core.dc.*;
import excs.UnknownCatchAlgorithm;
import excs.UnknownCatchAlgorithmId;
import excs.UnknownDCAlgorithm;
import excs.UnknownDCAlgorithmId;


/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class Config {
    // DC Algorithm
    // Huffman tree algorithm
    public static final byte DC_HUFFMAN = 0;

    private byte algId;

    /**
     * Set the dc algorithm id of current config
     * @param aid algorithm id
     */
    public void setDCAlgorithm(byte aid) {
        algId = aid;
    }

    /**
     * get the dc algorithm of current config
     * @return current config dc algorithm
     * @throws UnknownDCAlgorithmId
     */
    public DCAlgorithm getDCAlgorithm()
    throws UnknownDCAlgorithmId {
        return getDCAlgorithmById(algId);
    }

    /**
     * get algorithm id of dc
     * @param dc the dc algorithm
     * @return the id of dc algorithm
     * @throws UnknownDCAlgorithm
     */
    public static byte getDCAlgorithmId(DCAlgorithm dc)
    throws UnknownDCAlgorithm {
        if(dc instanceof DCHuffmanAlgorithm) {
            return DC_HUFFMAN;
        }

        throw new UnknownDCAlgorithm(dc);
    }

    /**
     * get the dc algorithm by algId
     * @param algId the algorithm id
     * @return new dc
     * @throws UnknownDCAlgorithmId
     */
    public static DCAlgorithm getDCAlgorithmById(byte algId)
    throws UnknownDCAlgorithmId {
        switch (algId) {
            case DC_HUFFMAN: return new DCHuffmanAlgorithm();
            default: throw new UnknownDCAlgorithmId(algId);
        }
    }


    // Catch Algorithm
    // ascii catch algorithm
    public static final byte CTH_ASCII = 0;
    public static final byte CTH_FIXED_BIT = 1;
    public static final byte CTH_FIXED_BYTE = 2;

    private byte cthId;

    /**
     * get ca id by ca
     * @param ca the catch algorithm
     * @return the id of the ca
     * @throws UnknownCatchAlgorithm
     */
    public static byte getCatchAlgorithmId(CatchAlgorithm ca)
    throws UnknownCatchAlgorithm {
        if(ca instanceof CatchASCIIAlgorithm) {
            return CTH_ASCII;
        } else if(ca instanceof CatchFixedBitsAlgorithm) {
            return CTH_FIXED_BIT;
        } else if(ca instanceof CatchFixedBytesAlgorithm) {
            return CTH_FIXED_BYTE;
        }
        throw new UnknownCatchAlgorithm(ca);
    }

    /**
     * set the catch algorithm id of current config
     * @param cid the id of catch algorithm
     */
    public void setCatchAlgorithm(byte cid) {
        cthId = cid;
    }

    /**
     * get the catch algorithm of current config
     * @return the catch algorithm
     * @throws UnknownCatchAlgorithmId
     */
    public CatchAlgorithm getCatchAlgorithm()
    throws UnknownCatchAlgorithmId {
        return getCatchAlgorithmById(cthId);
    }

    /**
     * get the catch algorithm by id
     * @param cthId the catch algorithm
     * @return the catch algorithm
     * @throws UnknownCatchAlgorithmId
     */
    public static CatchAlgorithm getCatchAlgorithmById(byte cthId)
    throws UnknownCatchAlgorithmId {
        switch (cthId) {
            case CTH_ASCII: return new CatchASCIIAlgorithm();
            case CTH_FIXED_BIT: return new CatchFixedBitsAlgorithm();
            case CTH_FIXED_BYTE: return new CatchFixedBytesAlgorithm();
            default: throw new UnknownCatchAlgorithmId(cthId);
        }
    }
}
