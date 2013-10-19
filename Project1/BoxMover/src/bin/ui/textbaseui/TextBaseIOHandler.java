package bin.ui.textbaseui;

import bin.io.IOHelper;
import bin.ui.generic.IOHandler;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class TextBaseIOHandler extends IOHandler {
    public void putString(String s) {
        IOHelper.putString(s);
    }

    public void putToken(char ch) {
        IOHelper.putChar(ch);
    }

    public void putEOL() {
        IOHelper.putEOL();
    }

    public String getStringLine() {
        return IOHelper.getLowerStringLine();
    }

    public void putError(String s) {
        putStringLine(s);
    }
}
