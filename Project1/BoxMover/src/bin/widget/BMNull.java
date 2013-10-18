package bin.widget;

import bin.Env;
/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class BMNull extends BMWidget {
    public BMNull(int x,int y) {
        super(Env.BLOCK_NUM_NULL, x, y, Env.TOKEN_NULL);
    }

    public boolean isPassable() {
        return false;
    }
}
