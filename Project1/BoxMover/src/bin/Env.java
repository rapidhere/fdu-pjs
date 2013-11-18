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
    public static final String MAP_DIR = "./maps";
    public static final String SAVE_DIR = "./save";

    public static String getMap(int level) {
        return MAP_DIR + "/" + level + ".map";
    }

    public static String getSaveFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-HH-mm-ss");
        return SAVE_DIR + "/" + sdf.format(new Date()) + ".save";
    }

    public static final int MAX_LEVEL = 9;

    public static final int
        PLAYER_FACE_NORTH = 5,
        PLAYER_FACE_SOUTH = 6,
        PLAYER_FACE_WEST = 7,
        PLAYER_FACE_EAST = 8;

    public static final int
        BLOCK_NUM_NULL = 0,
        BLOCK_NUM_WALL = 1,
        BLOCK_NUM_EMPTY = 2,
        BLOCK_NUM_BOX = 3,
        BLOCK_NUM_TARGET = 4,
        BLOCK_NUM_FILLED_TARGET = 9;

    public static final int
        BLOCK_TYPE_NULL = 0,
        BLOCK_TYPE_WALL = 1,
        BLOCK_TYPE_EMPTY = 2,
        BLOCK_TYPE_BOX = 3,
        BLOCK_TYPE_TARGET = 4,
        BLOCK_TYPE_PERSON = 5;

    public static final int
        DIRECTION_LEFT = 0,
        DIRECTION_RIGHT = 1,
        DIRECTION_UP = 2,
        DIRECTION_DOWN = 3;

    public static final int[] DIRECTIONS= {
        DIRECTION_LEFT,
        DIRECTION_RIGHT,
        DIRECTION_UP,
        DIRECTION_DOWN
    };

    public static final char
        TOKEN_BOX = '□',
        TOKEN_EMPTY = ' ',
        TOKEN_NULL = ' ',
        TOKEN_PERSON = '☆',
        TOKEN_TARGET_BLOCK_FILLED = '●',
        TOKEN_TARGET_BLOCK_EMPTY = '○',
        TOKEN_WALL = '■';

    public static final int
        CMD_UP = 0,
        CMD_DOWN = 1,
        CMD_RIGHT = 2,
        CMD_LEFT = 3,
        CMD_EXIT = 4,
        CMD_RESTART = 5,
        CMD_BACK = 6,
        CMD_CHOSE = 7,
        CMD_SAVE = 8,
        CMD_BACKMENU = 9,
        CMD_HELP = 10;
}
