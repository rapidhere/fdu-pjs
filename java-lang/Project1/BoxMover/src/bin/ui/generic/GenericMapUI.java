package bin.ui.generic;

import bin.widget.BMMap;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :      GenericMapUI
 * Version :    ver 0.1
 * Usage :      the generic interface of MapUI
 */
abstract public class GenericMapUI extends GenericUI {
    protected BMMap map;        // the map to draw
    protected String info;      // the info to show

    /**
     * Set up the MapUI
     * @param map the map to draw
     */
    public void setMap(BMMap map) {
        this.map = map;
    }

    /**
     * Set up the info to show to next draw
     * @param info the info to show
     */
    public void setInfo(String info) {
        this.info = info;
    }

    /**
     * Put error on the screen
     * @param err error message
     */
    abstract public void putError(String err);

    /**
     * Get the command indicated by user
     * @return
     */
    abstract public Command getCommand();
}
