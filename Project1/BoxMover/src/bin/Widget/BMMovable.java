package bin.Widget;

import bin.Env;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
abstract public class BMMovable extends BMWidget {
    public BMMovable(int type_id, int x, int y, char token) {
        super(type_id, x, y, token);
    }

    public boolean isPassable() {
        return false;
    }

    public int[] move(int dir) {
        return moveToDirection(getX(), getY(), dir);
    }

    public void moveTo(int x,int y) {
        pos_x = x;
        pos_y = y;
    }

    public void moveTo(int dir) {
        int pos[] = move(dir);
        moveTo(pos[0], pos[1]);
    }

    static public int[] moveToDirection(int x, int y, int dir) {
        int ret[] = {x, y};

        switch (dir) {
            case Env.DIRECTION_RIGHT:   ret[1] += 1; break;
            case Env.DIRECTION_LEFT:    ret[1] -= 1; break;
            case Env.DIRECTION_UP:      ret[0] -= 1; break;
            case Env.DIRECTION_DOWN:    ret[0] += 1; break;
        }
        return ret;
    }
}
