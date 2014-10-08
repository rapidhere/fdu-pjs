package excs;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class UnknownAlgorithmId extends RGZException {
    public UnknownAlgorithmId(int aid) {
        super("Unknown Algorithm id : " + aid + " , no such algorithm");
    }
}
