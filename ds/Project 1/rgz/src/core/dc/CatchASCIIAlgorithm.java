package core.dc;

import excs.DCException;
import javafx.util.Pair;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */

class ASCIIToken extends Token {
    @Override
    public int hashCode() {
        return ((Byte)getToken()).intValue();
    }

    @Override
    public int compareTo(Object o) {
        if(o.hashCode() < hashCode())
            return -1;
        else if(o.hashCode() > hashCode())
            return 1;
        return 0;
    }

    @Override
    public String toString() {
        return "" + (char)((Byte) getToken()).shortValue();
    }
}

public class CatchASCIIAlgorithm implements CatchAlgorithm <ASCIIToken> {
    @Override
    public byte[] dump(Token[] tokens)
    throws DCException {
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
            buffer[i + 4] = (Byte)tokens[i].getToken();

        return buffer;
    }

    @Override
    public Pair<ASCIIToken[], Integer> load(byte[] bytes, int offset, int length)
    throws DCException {
        // load length
        int tokenLength = 0;
        for(int i = 0;i < 4;i ++) {
            tokenLength |= (bytes[i + offset] & 0xff) << (8 * i);
        }

        ASCIIToken[] tokens = new ASCIIToken[tokenLength];
        // load tokens
        for(int i = 0;i < tokenLength;i ++) {
            tokens[i] = new ASCIIToken();
            tokens[i].setToken(bytes[i + offset + 4]);
        }

        return new Pair<>(tokens, offset + tokenLength + 4);
    }

    @Override
    public Pair<ASCIIToken[], Integer> parse(byte[] bytes, int offset, int length)
    throws DCException {
        ASCIIToken[] tokens = new ASCIIToken[length];
        for(int i = 0;i < length;i ++) {
            tokens[i] = new ASCIIToken();
            tokens[i].setToken(bytes[offset + i]);
        }
        return new Pair<>(tokens, offset + length);
    }

    @Override
    public byte[] merge(Token[] tokens) throws DCException {
        byte[] bytes = new byte[tokens.length];
        for(int i = 0;i < tokens.length;i ++)
            bytes[i] = (Byte)tokens[i].getToken();

        return bytes;
    }
}
