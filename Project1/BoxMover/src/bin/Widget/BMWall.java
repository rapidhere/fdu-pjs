package bin.Widget;

import bin.Env;
/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class BMWall extends BMWidget {
    public BMWall(int x,int y) {
        super(Env.BLOCK_NUM_WALL, x, y, Env.TOKEN_WALL);
    }

    public boolean isPassable() {
        return false;
    }
}
