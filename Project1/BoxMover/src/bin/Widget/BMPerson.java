package bin.Widget;

import bin.Env;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class BMPerson extends BMWidget {
    private int face_dir;

    public BMPerson(int x,int y) {
        this(x, y, Env.PLAYER_FACE_EAST);
    }

    public BMPerson(int x,int y, int face_dir) {
        super(Env.BLOCK_NUM_PERSON, x, y);
        setFace(face_dir);
    }

    public void setFace(int _f) {
        face_dir = _f;
    }

    public int getFace() {
        return face_dir;
    }

    public char getToken() {
        return Env.TOKEN_PERSON;
    }

    public boolean isPassable() {
        return false;
    }
}
