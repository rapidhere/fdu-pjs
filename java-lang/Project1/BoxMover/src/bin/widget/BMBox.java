package bin.widget;

import bin.Env;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :      BMBox
 * Version :    ver 0.1
 * Usage :      The box in the map
 */
public class BMBox extends BMMovable {
    /**
     * Constructor
     * @param x pos x
     * @param y pos y
     */
    public BMBox(int x,int y) {
        super(Env.BLOCK_TYPE_BOX, x, y);
    }
}
