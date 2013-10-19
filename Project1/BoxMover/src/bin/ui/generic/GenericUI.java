package bin.ui.generic;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
abstract public class GenericUI {
    protected IOHandler ioh = null;

    public void setIOHandler(IOHandler ioh) {
        this.ioh = ioh;
    }

    abstract void draw();

    public String getCommand() {
        return ioh.getStringLine();
    }

    public IOHandler getIOHandler() {
        return ioh;
    }
}
