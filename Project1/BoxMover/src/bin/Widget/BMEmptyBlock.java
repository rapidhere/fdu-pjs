package bin.Widget;

import bin.Env;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class BMEmptyBlock extends BMWidget {
    public BMEmptyBlock(int x,int y) {
        super(Env.BLOCK_NUM_EMPTY, x, y);
    }

    public char getToken() {
        return Env.TOKEN_EMPTY;
    }

    public boolean isPassable() {
        return true;
    }
}