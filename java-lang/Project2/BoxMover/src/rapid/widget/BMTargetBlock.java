package rapid.widget;

import rapid.Env;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :      BMTarget
 * Version :    ver 0.1
 * Usage :      The target block in the map
 */
public class BMTargetBlock extends BMContainer {
    /**
     * Constructor
     * @param x pos x
     * @param y pos y
     */
    public BMTargetBlock(int x,int y) {
        super(BMWidget.BLOCK_TYPE_TARGET, x, y);
    }

    /**
     * if the target is filled with a box?
     * @return
     */
    public boolean isFilled() {
        if(inner != null && inner.getTypeId() == Env.BLOCK_NUM_BOX) {
            return true;
        }
        return false;
    }
}
