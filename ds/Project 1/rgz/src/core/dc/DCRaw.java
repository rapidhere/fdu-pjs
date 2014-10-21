package core.dc;

import excs.DCException;

import java.util.ArrayList;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class DCRaw <T extends Token> implements DCAlgorithm<T>{
    @Override
    public byte[] compress(ArrayList<T> tokens, CatchAlgorithm<T> ca) throws DCException {
        return ca.dump(tokens);
    }

    @Override
    public ArrayList<T> decompress(byte[] bytes, int offset, int length, CatchAlgorithm<T> ca)
    throws DCException {
        return ca.load(bytes, offset, length).getKey();
    }
}
