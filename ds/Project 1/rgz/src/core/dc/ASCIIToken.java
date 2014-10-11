package core.dc;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class ASCIIToken extends Token {
    @Override
    public int hashCode() {
        return (Integer)getToken();
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
}
