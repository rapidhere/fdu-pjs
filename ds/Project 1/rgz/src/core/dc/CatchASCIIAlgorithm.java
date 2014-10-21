package core.dc;

import excs.DCException;
import javafx.util.Pair;

import java.util.ArrayList;

public class CatchASCIIAlgorithm implements CatchAlgorithm <ASCIIToken> {
    @Override
    public byte[] dump(ArrayList<ASCIIToken> tokens)
        throws DCException {
        // create buffer
        byte[] buffer = new byte[tokens.size() + 4];

        // dump length
        int length = tokens.size();
        for(int i = 0;i < 4;i ++) {
            buffer[i] = (byte)(length & 0xff);
            length >>= 8;
        }

        // dump tokens
        for(int i = 0;i < tokens.size();i ++)
            buffer[i + 4] = tokens.get(i).getToken();

        return buffer;
    }

    @Override
    public Pair<ArrayList<ASCIIToken>, Integer> load(byte[] bytes, int offset, int length)
        throws DCException {
        // load length
        int tokenLength = 0;
        for(int i = 0;i < 4;i ++) {
            tokenLength |= (bytes[i + offset] & 0xff) << (8 * i);
        }

        ArrayList<ASCIIToken> tokens = new ArrayList<>();
        // load tokens
        for(int i = 0;i < tokenLength;i ++) {
            tokens.add(new ASCIIToken());
            tokens.get(i).setToken(bytes[i + offset + 4]);
        }

        return new Pair<>(tokens, offset + tokenLength + 4);
    }

    @Override
    public Pair<ArrayList<ASCIIToken>, Integer> parse(byte[] bytes, int offset, int length)
        throws DCException {
        ArrayList<ASCIIToken> tokens = new ArrayList<>();
        for(int i = 0;i < length;i ++) {
            tokens.add(new ASCIIToken());
            tokens.get(i).setToken(bytes[i + offset]);
        }
        return new Pair<>(tokens, offset + length);
    }

    @Override
    public byte[] merge(ArrayList<ASCIIToken> tokens) throws DCException {
        byte[] bytes = new byte[tokens.size()];
        for(int i = 0;i < tokens.size();i ++)
            bytes[i] = tokens.get(i).getToken();

        return bytes;
    }
}
