package bin.Widget;

import bin.Env;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class BMTargetBlock extends BMWidget {
    boolean filled = false;

    public BMTargetBlock(int x,int y) {
        this(x, y, false);
    }

    public BMTargetBlock(int x,int y, boolean filled) {
        super(Env.BLOCK_NUM_TARGET, x, y);

        setFilled(filled);
    }

    public void setFilled(boolean flag) {
        filled = flag;
    }

    public boolean isFilled() {
        return filled;
    }

    public boolean isPassable() {
        return filled;
    }

    public char getToken() {
        if(isFilled()) {
            return Env.TOKEN_TARGET_BLOCK_FILLED;
        } else {
            return Env.TOKEN_TARGET_BLOCK_EMPTY;
        }
    }
}
