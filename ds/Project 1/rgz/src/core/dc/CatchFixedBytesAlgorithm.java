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
class FixedBytesToken extends Token{
    private byte byteLength;
    public FixedBytesToken(byte byteLength) {
        this.byteLength = byteLength;
    }

    @Override
    public int hashCode() {
        byte[] b = (byte[])getToken();
        long ret = 0;
        for(int i = 0;i < b.length;i ++) {
            ret *= 10007;
            ret += b[i];
            ret %= 1000000000 + 7;
        }

        return (int)ret;
    }

    @Override
    public int compareTo(Object o) {
        byte[] b1 = (byte[])getToken(),
            b2 = (byte[])getToken();

        assert b1.length == b2.length;
        assert b1.length == byteLength;

        for(int i = 0;i < byteLength;i ++) {
            if(b1[i] < b2[i])
                return -1;
            if(b1[i] > b2[i])
                return 1;
        }
        return 0;
    }
}

public class CatchFixedBytesAlgorithm implements CatchAlgorithm <FixedBytesToken> {
    public CatchFixedBytesAlgorithm() {}
    public CatchFixedBytesAlgorithm(byte byteLength) {
        setByteLength(byteLength);
    }

    private byte byteLength;
    public void setByteLength(byte byteLength) {
        this.byteLength = byteLength;
    }

    public byte getByteLength() {
        return this.byteLength;
    }

    @Override
    public byte[] dump(Token[] tokens) throws DCException {
        byte[] bytes = new byte[tokens.length * byteLength + 4];
        // write length
        int tokenLength = tokens.length;
        for(int i = 0;i < 4; ++ i) {
            bytes[i] = (byte)(tokenLength & 0xff);
            tokenLength >>= 8;
        }

        // write tokens
        for(int i = 0;i < tokens.length;i ++) {
            System.arraycopy(tokens[i].getToken(), 0, bytes, 4 + i * byteLength, byteLength);
        }

        return bytes;
    }

    @Override
    public Pair<FixedBytesToken[], Integer> load(byte[] bytes, int offset, int length)
    throws DCException {
        if(length < 4)
            throw new DCException("load failed: cannot load size info");
        // load length
        int tokenLength = 0;
        for(int i = 0;i < 4;i ++)
            tokenLength |= (bytes[i + offset] & 0xff) << (i * 8);

        if(length < 0 || length < tokenLength * byteLength + 4)
            throw new DCException("load failed: wrong length info");

        // load token
        FixedBytesToken[] tokens = new FixedBytesToken[tokenLength];
        for(int i = 0;i < tokens.length;i ++) {
            byte[] cb = new byte[byteLength];
            System.arraycopy(bytes, offset + 4 + i * byteLength, cb, 0, byteLength);
            tokens[i] = new FixedBytesToken(byteLength);
            tokens[i].setToken(cb);
        }

        return new Pair<FixedBytesToken[], Integer>(tokens, offset + 4 + tokens.length * byteLength);
    }

    @Override
    public Pair<FixedBytesToken[], Integer> parse(byte[] bytes, int offset, int length)
    throws DCException {
        int tokenLength = length / byteLength;
        FixedBytesToken[] tokens = new FixedBytesToken[tokenLength];
        for(int i = 0;i < tokenLength;i ++) {
            byte[] cb = new byte[byteLength];
            System.arraycopy(bytes, offset + i * byteLength, cb, 0, byteLength);
            tokens[i] = new FixedBytesToken(byteLength);
            tokens[i].setToken(cb);
        }

        return new Pair<FixedBytesToken[], Integer>(tokens, offset + tokenLength * byteLength);
    }

    @Override
    public byte[] merge(Token[] tokens) throws DCException {
        byte[] bytes = new byte[tokens.length * byteLength];

        for(int i = 0;i < tokens.length;i ++) {
            System.arraycopy(tokens[i].getToken(), 0, bytes, i * byteLength, byteLength);
        }

        return bytes;
    }
}
