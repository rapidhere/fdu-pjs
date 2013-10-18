package bin.Widget;

import bin.Env;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class BMPerson extends BMMovable {
    private int face_dir;

    public BMPerson(int x,int y) {
        this(x, y, Env.PLAYER_FACE_EAST);
    }

    public BMPerson(int x,int y, int face_dir) {
        super(Env.BLOCK_NUM_PERSON, x, y, Env.TOKEN_PERSON);
        setFace(face_dir);
    }

    public void setFace(int _f) {
        face_dir = _f;
    }

    public int getFace() {
        return face_dir;
    }

    public boolean isPassable() {
        return false;
    }
}
