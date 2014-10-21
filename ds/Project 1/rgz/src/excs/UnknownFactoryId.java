package excs;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class UnknownFactoryId extends RGZException {
    public UnknownFactoryId(int tid) {
        super("Unknown Factory id : " + tid + " , no such token");
    }
}
