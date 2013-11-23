package bin.widget;

import java.io.Serializable;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :      BMWidget
 * Version :    ver 0.1
 * Usage :      The generic Widget in one cell of map
 *              For more info please refer to Developer Manual
 */

abstract public class BMWidget implements Serializable {
    protected int pos_x, pos_y;     // the position of widget
    protected int type_id;          // the type_id of widget, see Env.BLOCK_TYPE_*

    /**
     * Constructor
     * @param type_id the type_id of the widget, see Env.BLOCK_TYPE_*
     * @param x position x
     * @param y position y
     */
    public BMWidget(int type_id, int x, int y) {
        this.type_id = type_id;
        pos_x = x;
        pos_y = y;
    }

    /**
     * get the type_id
     * @return the type_id of widget, see Env.BLOCK_TYPE_*
     */
    public int getTypeId() {
        return type_id;
    }

    /**
     * get the position x
     * @return position x
     */
    public int getX() {
        return pos_x;
    }

    /**
     * get the position y
     * @return position y
     */
    public int getY() {
        return pos_y;
    }

    /**
     * is widget Passable?
     * @return
     */
    abstract public boolean isPassable();
}
