package bin;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class : Env
 * Version : ver 0.1
 * Usage : Define the Environment of the game
 */

public class Env {
    /* The directory location of maps */
    public static final String MAP_DIR = "./maps";

    public static String getMap(int level) {
        return MAP_DIR + "/" + level + ".map";
    }

    /* Where to save games */
    public static final String SAVE_DIR = "./save";

    public static String getSaveFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-HH-mm-ss");
        return SAVE_DIR + "/" + sdf.format(new Date()) + ".save";
    }

    /* Max Level of the game */
    public static final int MAX_LEVEL = 9;

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

    /* The type_ids of BMWidgets */
    public static final int
        BLOCK_TYPE_NULL = 0,
        BLOCK_TYPE_WALL = 1,
        BLOCK_TYPE_EMPTY = 2,
        BLOCK_TYPE_BOX = 3,
        BLOCK_TYPE_TARGET = 4,
        BLOCK_TYPE_PERSON = 5;

    /* 4 Directions */
    public static final int
        DIRECTION_LEFT = 0,
        DIRECTION_RIGHT = 1,
        DIRECTION_UP = 2,
        DIRECTION_DOWN = 3;

    /* All Direction as a array */
    public static final int[] DIRECTIONS= {
        DIRECTION_LEFT,
        DIRECTION_RIGHT,
        DIRECTION_UP,
        DIRECTION_DOWN
    };

    /* Commands when playing game */
    public static final int
        CMD_UP = 0,             // move up
        CMD_DOWN = 1,           // move down
        CMD_RIGHT = 2,          // move right
        CMD_LEFT = 3,           // move left
        CMD_EXIT = 4,           // exit application
        CMD_RESTART = 5,        // restart level
        CMD_BACK = 6,           // back steps
        CMD_CHOSE = 7,          // chose level
        CMD_SAVE = 8,           // save game
        CMD_BACKMENU = 9,       // back to main menu
        CMD_HELP = 10;          // help
}
