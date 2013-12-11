package rapid.exp;

import rapid.exp.BoxMoverException;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
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
