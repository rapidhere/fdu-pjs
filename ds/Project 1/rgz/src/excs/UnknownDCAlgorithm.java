package excs;

import core.dc.DCAlgorithm;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class UnknownDCAlgorithm extends RGZException {
    public UnknownDCAlgorithm(Class<? extends DCAlgorithm> dcClass) {
        super("Unknown DC Algorithm: " + dcClass.getName());
    }
}
