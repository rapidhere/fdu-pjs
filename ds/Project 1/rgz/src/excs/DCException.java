package excs;

import java.io.IOException;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class DCException extends RGZException {
    public DCException(String msg) {
        super("Error occurred when Compress/Decompress: " + msg);
    }

    public DCException(IOException ioe) {
        this("IOError: " + ioe.getMessage());
    }
}
