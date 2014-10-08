package bin.widget;

import bin.Env;
/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :      BMWall
 * Version :    ver 0.1
 * Usage :      The wall in the map
 */
public class BMWall extends BMWidget {
    /**
     * Constructor
     * @param x pos x
     * @param y pos y
     */
    public BMWall(int x,int y) {
        super(Env.BLOCK_TYPE_WALL, x, y);
    }

    public boolean isPassable() {
        return false;
    }
}
