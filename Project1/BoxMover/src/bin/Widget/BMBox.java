package bin.Widget;

import bin.Env;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class BMBox extends BMWidget {
    public BMBox(int x,int y) {
        super(Env.BLOCK_NUM_BOX, x, y);
    }

    public char getToken() {
        return Env.TOKEN_BOX;
    }

    public boolean isPassable() {
        return false;
    }
}
