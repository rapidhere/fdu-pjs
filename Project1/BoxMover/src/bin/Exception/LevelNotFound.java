package bin.Exception;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class LevelNotFound extends BMException {
    private int level;

    public LevelNotFound(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
