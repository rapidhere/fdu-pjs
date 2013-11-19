package bin.widget;

import bin.Env;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class BMTargetBlock extends BMContainer {
    public BMTargetBlock(int x,int y) {
        super(Env.BLOCK_TYPE_TARGET, x, y);
    }

    public boolean isFilled() {
        if(inner != null && inner.getTypeId() == Env.BLOCK_NUM_BOX) {
            return true;
        }
        return false;
    }
}
