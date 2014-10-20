package core.dc;

import excs.DCException;
import javafx.util.Pair;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public interface CatchAlgorithm <T extends Token> {
    /**
     * Dump list of tokens into bytes and then we can retrieve these tokens from load function
     * NOTE: this function isn't used to dump tokens into original format
     *       compress and decompress function can use this function to dump bytes sequence if
     *       there dc info have token sequence
     * @param tokens tokens to dump
     * @return dumped bytes
     * @exception excs.DCException
     */
    byte[] dump(T[] tokens) throws DCException;

    /**
     * Load token list from bytes array generated by dump function
     * NOTE: this function isn't used to load tokens from original format
     *      compress and decompress function can use this function to load tokens if
     *      there dc info have token sequence
     * @param bytes byte array
     * @param offset start offset
     * @param length length of byte
     * @return tokens list and remain offset
     * @exception excs.DCException
     */
    Pair<T[], Integer> load(byte[] bytes, int offset, int length) throws DCException;

    /**
     * parse token list from bytes array
     * @param bytes byte array
     * @param offset start offset
     * @param length length of byte
     * @return tokens list and remain offset
     * @exception DCException
     */
    Pair<T[], Integer> parse(byte[] bytes, int offset, int length) throws DCException;

    /**
     * merge a list of tokens back to original bytes
     * @param tokens tokens to merge
     * @return merged sequence
     * @throws DCException
     */
    byte[] merge(T[] tokens) throws DCException;

    default void dumpHeader(OutputStream out) throws DCException {}

    default void loadHeader(InputStream in) throws DCException {}
}

