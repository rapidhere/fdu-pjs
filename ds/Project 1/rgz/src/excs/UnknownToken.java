package excs;

import core.dc.Token;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class UnknownToken extends RGZException {
    public UnknownToken(Class<? extends Token> tkClass) {
        super("Unknown Token Class: " + tkClass.getName());
    }
}
