package rapid.exp;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class : LevelNotFound
 * Version : 0.1
 * Usage : Cannot found the specified level file
 */
public class LevelNotFound extends BoxMoverException {
    private int level;

    public LevelNotFound(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
