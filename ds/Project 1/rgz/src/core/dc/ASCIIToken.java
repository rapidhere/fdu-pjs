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

public class ASCIIToken extends Token <Byte> {
    @Override
    public int hashCode() {
        return getToken().intValue();
    }

    @Override
    public int compareTo(@SuppressWarnings("NullableProblems") Token<Byte> o) {
        if(o.hashCode() < hashCode())
            return -1;
        else if(o.hashCode() > hashCode())
            return 1;
        return 0;
    }

    @Override
    public String toString() {
        return "" + (char)getToken().shortValue();
    }
}

