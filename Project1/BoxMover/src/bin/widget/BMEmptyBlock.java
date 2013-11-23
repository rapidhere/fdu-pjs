package bin.widget;

import bin.Env;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :      BMEmptyBlock
 * Version :    ver 0.1
 * Usage :      The empty block in the map
 */
public class BMEmptyBlock extends BMContainer {
    /**
     * Constructor
     * @param x pos x
     * @param y pos y
     */
    public BMEmptyBlock(int x,int y) {
        super(Env.BLOCK_TYPE_EMPTY, x, y);
    }
}