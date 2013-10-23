package bin.ui.generic;

import bin.widget.BMMap;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
abstract public class GenericMapUI extends GenericUI {
    protected BMMap map;
    protected int cur_step;
    protected String info;

    public void setMap(BMMap map) {
        this.map = map;
    }

    public void setCurStep(int t) {
        cur_step = t;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    abstract public void putError(String err);
    abstract public Command getCommand();
}
