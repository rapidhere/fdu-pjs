package ui;

import core.dc.*;
import excs.*;


/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
final public class Config {
    // DCM Algorithm
    // Huffman tree algorithm
    public static final byte DC_HUFFMAN = 0;

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

    // DC_MAN
    public static final byte DCM_BLOCK = 0;

    /**
     * get dcm id
     * @param dcm the dcm
     * @return dcm id
     * @throws UnknownDCM
     */
    public static byte getDCMId(DCM dcm)
    throws UnknownDCM {
        if(dcm instanceof BlockDCM) {
            return DCM_BLOCK;
        }

        throw new UnknownDCM(dcm);
    }

    /**
     * get dcm by id
     * @param dcmId dcm id
     * @return new DCM
     * @throws UnknownDCMId
     */
    public static DCM getDCMbyId(byte dcmId)
    throws UnknownDCMId {
        switch(dcmId) {
            case DCM_BLOCK: return new BlockDCM();
            default: throw new UnknownDCMId(dcmId);
        }
    }
}
