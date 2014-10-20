package core.dc;

import excs.DCException;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
abstract public interface DCAlgorithm <T extends Token> {
    abstract public byte[] compress(T[] tokens, CatchAlgorithm<T> ca) throws DCException;
    abstract public T[] decompress(byte[] bytes, int offset, int length, CatchAlgorithm<T> ca) throws DCException;

    abstract byte getDCId();
}
