package excs;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class UnknownDCAlgorithmId extends RGZException {
    public UnknownDCAlgorithmId(int aid) {
        super("Unknown DC Algorithm id : " + aid + " , no such algorithm");
    }
}
