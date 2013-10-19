package bin.ui.generic;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public abstract class IOHandler {
    abstract public void putString(String s);
    abstract public String getStringLine();
    abstract public void putToken(char ch);
    abstract public void putEOL();
    abstract public void putError(String s);

    public void putStringLine(String s) {
        putString(s);
        putEOL();
    }
}
