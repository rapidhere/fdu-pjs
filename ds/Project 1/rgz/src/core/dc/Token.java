package core.dc;

import javafx.util.Pair;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
abstract public class Token implements Comparable {
    private Object token;

    public abstract byte[] dumpTokens (Token[] tokens);
    public abstract Pair<Token[], Integer> loadTokens(byte[] bytes, int offset);
    public abstract int hashCode();

    public Object getToken() {
        return token;
    }

    public void setToken(Object o) {
        token = o;
    }
}
