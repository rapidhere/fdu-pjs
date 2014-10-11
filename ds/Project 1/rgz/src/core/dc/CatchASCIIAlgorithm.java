package core.dc;

import javafx.util.Pair;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class CatchASCIIAlgorithm implements CatchAlgorithm {
    public class ASCIIToken extends Token {
        @Override
        public int hashCode() {
            return ((Byte)getToken()).intValue();
        }

        @Override
        public byte[] dump() {
            byte[] ret = new byte[1];
            ret[0] = (Byte)getToken();
            return ret;
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
            return "" + (char)((Byte)getToken()).byteValue();
        }
    }

    @Override
    public byte[] dump(Token[] tokens) {
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
    public Pair<Token[], Integer> load(byte[] bytes, int offset, int length) {
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

        return new Pair<Token[], Integer>(tokens, offset + tokenLength + 4);
    }

    @Override
    public Pair<Token[], Integer> parse(byte[] bytes, int offset, int length) {
        ASCIIToken[] tokens = new ASCIIToken[length];
        for(int i = 0;i < length;i ++) {
            tokens[i] = new ASCIIToken();
            tokens[i].setToken(bytes[offset + i]);
        }
        return new Pair<Token[], Integer>(tokens, offset + length);
    }
}
