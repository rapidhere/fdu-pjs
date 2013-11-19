package bin.widget;

import java.io.Serializable;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
abstract public class BMWidget implements Serializable {
    protected int pos_x, pos_y;
    protected int type_id;

    public BMWidget(int type_id, int x, int y) {
        this.type_id = type_id;
        pos_x = x;
        pos_y = y;
    }

    public int getTypeId() {
        return type_id;
    }

    public int getX() {
        return pos_x;
    }

    public int getY() {
        return pos_y;
    }

    abstract public boolean isPassable();
}
