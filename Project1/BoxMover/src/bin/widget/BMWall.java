package bin.widget;

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
        super(Env.BLOCK_TYPE_WALL, x, y);
    }

    public boolean isPassable() {
        return false;
    }
}
