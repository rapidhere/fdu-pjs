package core.dc;

import excs.DCException;
import javafx.util.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class CatchFixedBytesAlgorithm implements CatchAlgorithm <FixedBytesToken> {
    public CatchFixedBytesAlgorithm() {}

    private byte byteLength;
    public void setByteLength(byte byteLength) {
        this.byteLength = byteLength;
    }

    public byte getByteLength() {
        return this.byteLength;
    }

    @Override
    synchronized public byte[] dump(ArrayList<FixedBytesToken> tokens) throws DCException {
        byte[] bytes = new byte[tokens.size() * byteLength + 4];
        // write length
        int tokenLength = tokens.size();
        for(int i = 0;i < 4; ++ i) {
            bytes[i] = (byte)(tokenLength & 0xff);
            tokenLength >>= 8;
        }

        // write tokens
        for(int i = 0;i < tokens.size();i ++) {
            System.arraycopy(tokens.get(i).getToken(), 0, bytes, 4 + i * byteLength, byteLength);
        }

        return bytes;
    }

    @Override
    synchronized public Pair<ArrayList<FixedBytesToken>, Integer> load(byte[] bytes, int offset, int length)
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
        ArrayList<FixedBytesToken> tokens = new ArrayList<>();
        for(int i = 0;i < tokenLength;i ++) {
            byte[] cb = new byte[byteLength];
            System.arraycopy(bytes, offset + 4 + i * byteLength, cb, 0, byteLength);
            tokens.add(new FixedBytesToken(byteLength));
            tokens.get(i).setToken(cb);
        }

        return new Pair<>(tokens, offset + 4 + tokens.size() * byteLength);
    }

    @Override
    synchronized public Pair<ArrayList<FixedBytesToken>, Integer> parse(byte[] bytes, int offset, int length)
        throws DCException {
        int tokenLength = length / byteLength;
        ArrayList<FixedBytesToken> tokens = new ArrayList<>();
        for(int i = 0;i < tokenLength;i ++) {
            byte[] cb = new byte[byteLength];
            System.arraycopy(bytes, offset + i * byteLength, cb, 0, byteLength);
            tokens.add(new FixedBytesToken(byteLength));
            tokens.get(i).setToken(cb);
        }

        return new Pair<>(tokens, offset + tokenLength * byteLength);
    }

    @Override
    synchronized public byte[] merge(ArrayList<FixedBytesToken> tokens) throws DCException {
        byte[] bytes = new byte[tokens.size() * byteLength];

        for(int i = 0;i < tokens.size();i ++) {
            System.arraycopy(tokens.get(i).getToken(), 0, bytes, i * byteLength, byteLength);
        }

        return bytes;
    }

    @Override
    public void dumpHeader(OutputStream out)
        throws DCException {
        try {
            out.write(getByteLength());
        } catch (IOException e) {
            throw new DCException("dump header failed: " + e.getMessage());
        }
    }

    @Override
    public void loadHeader(InputStream in)
        throws DCException {
        try {
            int b = in.read();
            if(b == -1)
                throw new DCException("Wrong file syntax: cannot load dc ca info!");
            setByteLength((byte) b);
        } catch (IOException e) {
            throw new DCException("load header failed: " + e.getMessage());
        }
    }
}
