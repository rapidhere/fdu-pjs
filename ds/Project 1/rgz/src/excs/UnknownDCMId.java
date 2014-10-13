package excs;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class UnknownDCMId extends RGZException {
    public UnknownDCMId(int dcmId) {
        super("Unknown DCM id: " + dcmId);
    }
}
