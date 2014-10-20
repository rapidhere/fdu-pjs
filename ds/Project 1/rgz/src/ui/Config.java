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
    public static String version = "ver 0.1";

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
    public static Class<? extends DCAlgorithm> getDCAlgorithmById(byte algId)
    throws UnknownDCAlgorithmId {
        switch (algId) {
            case DC_HUFFMAN: return DCHuffmanAlgorithm.class;
            default: throw new UnknownDCAlgorithmId(algId);
        }
    }


    // Token IDs
    public static final byte TOKEN_ASCII = 0;
    public static final byte TOKEN_FIXED_BIT = 1;
    public static final byte TOKEN_FIXED_BYTE = 2;

    /**
     * get ca id by ca
     * @param tkClass the catch algorithm
     * @return the id of the ca
     * @throws excs.UnknownToken
     */
    public static byte getTokenId(Class<? extends Token> tkClass)
    throws UnknownToken {
        if(tkClass.equals(ASCIIToken.class)) {
            return TOKEN_ASCII;
        } else if(tkClass.equals(FixedBitsToken.class)) {
            return TOKEN_FIXED_BIT;
        } else if(tkClass.equals(FixedBytesToken.class)) {
            return TOKEN_FIXED_BYTE;
        }
        throw new UnknownToken(tkClass);
    }

    /**
     * get the catch algorithm by id
     * @param tkId the catch algorithm
     * @return the catch algorithm
     * @throws excs.UnknownTokenId
     */
    public static Class<? extends Token> getTokenById(byte tkId)
    throws UnknownTokenId {
        switch (tkId) {
            case TOKEN_ASCII: return ASCIIToken.class;
            case TOKEN_FIXED_BIT: return FixedBitsToken.class;
            case TOKEN_FIXED_BYTE: return FixedBytesToken.class;
            default: throw new UnknownTokenId(tkId);
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
