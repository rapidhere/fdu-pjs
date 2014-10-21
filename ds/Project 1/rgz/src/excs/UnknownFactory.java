package excs;

import core.dc.DCFactory;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class UnknownFactory extends RGZException {
    public UnknownFactory(Class<? extends DCFactory> fcClass) {
        super("Unknown DCFactory Class: " + fcClass.getName());
    }
}
