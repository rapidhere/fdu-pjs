package bin;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class Env {
    public static final String MAP_DIR = "./maps";

    public static String getMap(int level) {
        return MAP_DIR + "/" + level + ".map";
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
        BLOCK_NUM_PERSON = PLAYER_FACE_EAST,
        BLOCK_NUM_FILLED_TARGET = 9;

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
        CMD_TYPE_NO_ARG = 0,
        CMD_TYPE_INT_OPT_ARG = 1,
        CMD_TYPE_INT_RANGE_ARG = 2;

    public static final String
        CMD_UP = "w",
        CMD_DOWN = "s",
        CMD_RIGHT = "d",
        CMD_LEFT = "a",
        CMD_EXIT = "exit",
        CMD_RESTART = "restart",
        CMD_BACK = "back",
        CMD_CHOSE = "chose",
        CMD_HELP = "help";

    public static final Object[][] COMMANDS = {
        {CMD_UP,        CMD_TYPE_INT_OPT_ARG},
        {CMD_DOWN,      CMD_TYPE_INT_OPT_ARG},
        {CMD_RIGHT,     CMD_TYPE_INT_OPT_ARG},
        {CMD_LEFT,      CMD_TYPE_INT_OPT_ARG},
        {CMD_EXIT,      CMD_TYPE_NO_ARG},
        {CMD_RESTART,   CMD_TYPE_NO_ARG},
        {CMD_BACK,      CMD_TYPE_INT_OPT_ARG},
        {CMD_CHOSE,     CMD_TYPE_INT_RANGE_ARG},
        {CMD_HELP,      CMD_TYPE_NO_ARG},
    };
}
