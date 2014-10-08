package bin.widget;

import bin.Env;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :      BMPerson
 * Version :    ver 0.1
 * Usage :      The Bot in the map
 */
public class BMPerson extends BMMovable {
    private int face_dir;           // the face direction of player

    /**
     * Constructor
     * @param x pos x
     * @param y pos y
     */
    public BMPerson(int x,int y) {
        this(x, y, Env.PLAYER_FACE_EAST);
    }

    /**
     * Constructor
     * @param x pos x
     * @param y pos y
     * @param face_dir the face direction of player
     */
    public BMPerson(int x,int y, int face_dir) {
        super(Env.BLOCK_TYPE_PERSON, x, y);
        setFace(face_dir);
    }

    /**
     * Set the face direction of player
     * @param _f  new face direction
     */
    public void setFace(int _f) {
        face_dir = _f;
    }

    /**
     * get the face direction of player
     * @return the face direction of player
     */
    public int getFace() {
        return face_dir;
    }

    public boolean isPassable() {
        return false;
    }
}
