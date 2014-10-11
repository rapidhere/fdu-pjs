package core.dc;

import javafx.util.Pair;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class CatchASCIIAlgorithm implements CatchAlgorithm <ASCIIToken> {
    @Override
    public byte[] dump(ASCIIToken[] tokens) {
        // create buffer
        byte[] buffer = new byte[tokens.length + 4];

        // dump length
        int length = tokens.length;
        for(int i = 0;i < 4;i ++) {
            buffer[i] = (byte)(length & 0xff);
            length >>= 8;
        }

        // dump tokens
        for(int i = 0;i < tokens.length;i ++)
            buffer[i + 4] = tokens[i].dump()[0];

        return buffer;
    }

    @Override
    public Pair<ASCIIToken[], Integer> load(byte[] bytes, int offset, int length) {
        // load length
        int tokenLength = 0;
        for(int i = 0;i < 4;i ++) {
            tokenLength |= ((int)bytes[i + offset]) << (8 * i);
        }

        ASCIIToken[] tokens = new ASCIIToken[tokenLength];
        // load tokens
        for(int i = 0;i < tokenLength;i ++)
            tokens[i].setToken(bytes[i + offset + 4]);

        return new Pair<ASCIIToken[], Integer>(tokens, offset + length);
    }

    @Override
    public Pair<ASCIIToken[], Integer> parse(byte[] bytes, int offset, int length) {
        ASCIIToken[] tokens = new ASCIIToken[length];
        for(int i = 0;i < length;i ++)
            tokens[i].setToken(bytes[offset + i]);
        return new Pair<ASCIIToken[], Integer>(tokens, offset + length);
    }
}
