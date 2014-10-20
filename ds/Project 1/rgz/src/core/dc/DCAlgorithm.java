package core.dc;

import excs.DCException;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
abstract public interface DCAlgorithm {
    abstract public byte[] compress(Token[] tokens, CatchAlgorithm ca) throws DCException;
    abstract public Token[] decompress(byte[] bytes, int offset, int length, CatchAlgorithm<Token> ca) throws DCException;
}
