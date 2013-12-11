package rapid;

import java.security.PublicKey;
import java.util.regex.Pattern;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class Env {
    public final static String PIC_DIRECTORY = "./pic/";
    public final static String USR_DIRECTORY = "./usr/";
    public static final String MAP_DIRECTORY = "./maps/";

    public final static String FRAME_TITLE = "BoxMover";

    public final static String NEW_USER_TITLE = "Create a New User";
    public final static Pattern NEW_USER_NAME_CHECKER = Pattern.compile("[a-zA-Z]\\w*");
    public final static String NEW_USER_WRONG_NAME_MSG = "Illegal User Name!";
    public final static String NEW_USER_USER_NAME_EXISTS = "User Name has been used!";

    /* The face direction of bot */
    public static final int
        PLAYER_FACE_NORTH = 5,
        PLAYER_FACE_SOUTH = 6,
        PLAYER_FACE_WEST = 7,
        PLAYER_FACE_EAST = 8;

    /* The original code info in map files */
    public static final int
        BLOCK_NUM_NULL = 0,
        BLOCK_NUM_WALL = 1,
        BLOCK_NUM_EMPTY = 2,
        BLOCK_NUM_BOX = 3,
        BLOCK_NUM_TARGET = 4,
        BLOCK_NUM_FILLED_TARGET = 9;

    public static final int BLOCK_SIZE = 30;
}
