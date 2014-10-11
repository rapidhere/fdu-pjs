package excs;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class UnknownCatchAlgorithmId extends RGZException {
    public UnknownCatchAlgorithmId(int cid) {
        super("Unknown Catch Algorithm id : " + cid + " , no such algorithm");
    }
}
