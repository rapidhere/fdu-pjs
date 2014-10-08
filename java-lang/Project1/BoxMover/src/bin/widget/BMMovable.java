package bin.widget;

import bin.Env;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :      BMMovable
 * Version :    ver 0.1
 * Usage :      The generic class of widgets that can move on the map
 */

abstract public class BMMovable extends BMWidget {
    /**
     * Constructor
     * @param type_id the type_id of widget, see ENV.BLOCK_TYPE_*
     * @param x position x
     * @param y position y
     */
    public BMMovable(int type_id, int x, int y) {
        super(type_id, x, y);
    }

    public boolean isPassable() {
        return false;
    }

    /**
     * Trying to move to next position specified by direction
     * @param dir the direction, ENV.DIRECTION_*
     * @return the next position in [x,y]
     */
    public int[] move(int dir) {
        return moveToDirection(getX(), getY(), dir);
    }

    /**
     * Trying to move back from the direction
     * @param dir the direction, ENV.DIRECTION_*
     * @return the next position in [x,y]
     */
    public int[] moveBack(int dir) {
        return moveBackDirection(getX(), getY(), dir);
    }

    /**
     * move to specified position
     * @param x pos x
     * @param y pos y
     */
    public void moveTo(int x,int y) {
        pos_x = x;
        pos_y = y;
    }

    /**
     * move to specified direction
     * @param dir  Env.DIRECTION_*
     */
    public void moveTo(int dir) {
        int pos[] = move(dir);
        moveTo(pos[0], pos[1]);
    }

    // Low layer move functions
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

    static public int[] moveBackDirection(int x,int y,int dir) {
        switch (dir) {
            case Env.DIRECTION_RIGHT:   dir = Env.DIRECTION_LEFT; break;
            case Env.DIRECTION_LEFT:    dir = Env.DIRECTION_RIGHT; break;
            case Env.DIRECTION_UP:      dir = Env.DIRECTION_DOWN; break;
            case Env.DIRECTION_DOWN:    dir = Env.DIRECTION_UP; break;
        }

        return moveToDirection(x, y, dir);
    }
}
