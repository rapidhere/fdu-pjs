package excs;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class UnknownTokenId extends RGZException {
    public UnknownTokenId(int tid) {
        super("Unknown Token id : " + tid + " , no such token");
    }
}
