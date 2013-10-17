package bin.Widget;

import bin.Env;
/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
abstract public class BMWidget {
    protected int pos_x, pos_y;
    protected int type_id;

    public BMWidget(int type_id, int x, int y) {
        this.type_id = type_id;
        pos_x = x;
        pos_y = y;
    }

    public int getTypeId() {
        return type_id;
    }

    public int getX() {
        return pos_x;
    }

    public int getY() {
        return pos_y;
    }

    public void move(int dir) {
        switch (dir) {
            case Env.DIRECTION_RIGHT:   pos_y += 1; break;
            case Env.DIRECTION_LEFT:    pos_y -= 1; break;
            case Env.DIRECTION_UP:      pos_x -= 1; break;
            case Env.DIRECTION_DOWN:    pos_x += 1; break;
        }
        return;
    }

    abstract public char getToken();
    abstract public boolean isPassable();
}
