package bin.widget;

import bin.Env;
/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :      BMNull
 * Version :    ver 0.1
 * Usage :      the null blocks out of walls
 */
public class BMNull extends BMWidget {
    /**
     * Constructor
     * @param x pos x
     * @param y pos y
     */
    public BMNull(int x,int y) {
        super(Env.BLOCK_TYPE_NULL, x, y);
    }

    public boolean isPassable() {
        return false;
    }
}
