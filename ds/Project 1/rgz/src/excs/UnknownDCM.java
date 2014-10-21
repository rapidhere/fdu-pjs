package excs;

import core.dc.DCM;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class UnknownDCM extends RGZException {
    public UnknownDCM(Class<? extends DCM> dcmClass) {
        super("Unknown DCM Algorithm: " + dcmClass.getName());
    }
}
