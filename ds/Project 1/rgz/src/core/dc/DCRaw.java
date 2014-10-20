package core.dc;

import excs.DCException;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class DCRaw implements DCAlgorithm{
    @Override
    public byte[] compress(Token[] tokens, CatchAlgorithm ca) throws DCException {
        return ca.dump(tokens);
    }

    @Override
    public Token[] decompress(byte[] bytes, int offset, int length, CatchAlgorithm<Token> ca) throws DCException {
        return ca.load(bytes, offset, length).getKey();
    }
}
