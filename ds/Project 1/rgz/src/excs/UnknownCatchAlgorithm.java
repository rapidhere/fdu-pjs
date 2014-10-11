package excs;

import core.dc.CatchAlgorithm;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class UnknownCatchAlgorithm extends RGZException {
    public UnknownCatchAlgorithm(CatchAlgorithm ca) {
        super("Unknown Catch Algorithm: " + ca.getClass().getName());
    }
}
