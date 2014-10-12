package excs;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class TarException extends RGZException {
    public TarException(String msg) {
        super("Tar failed: " + msg);
    }
}
