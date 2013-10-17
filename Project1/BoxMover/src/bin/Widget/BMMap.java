package bin.Widget;

import bin.Exception.LevelNotFound;
import bin.IOHelper.IOHelper;
import bin.Env;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class BMMap {
    private int width, height;
    BMWidget map[][];

    public void loadMap(int level)
    throws LevelNotFound {
        IOHelper ioh = new IOHelper();
        ioh.readMap(level);

        width = ioh.getWidth();
        height = ioh.getHeight();

        map = new BMWidget[height][width];

        for(int i = 0;i < height;i ++) {
            for(int j = 0;j < width;j ++){
                BMWidget cur = null;
                int cval = ioh.getMapAt(i, j);
                switch (cval) {
                    case Env.PLAYER_FACE_EAST:
                    case Env.PLAYER_FACE_NORTH:
                    case Env.PLAYER_FACE_SOUTH:
                    case Env.PLAYER_FACE_WEST:
                        cur = new BMPerson(i, j, cval);
                        break;
                    case Env.BLOCK_NUM_BOX:
                        cur = new BMBox(i, j);
                        break;
                    case Env.BLOCK_NUM_EMPTY:
                        cur = new BMEmptyBlock(i, j);
                        break;
                    case Env.BLOCK_NUM_TARGET:
                        cur = new BMTargetBlock(i, j);
                        break;
                    case Env.BLOCK_NUM_FILLED_TARGET:
                        cur = new BMTargetBlock(i, j, true);
                        break;
                    case Env.BLOCK_NUM_WALL:
                        cur = new BMWall(i, j);
                        break;
                    case Env.BLOCK_NUM_NULL:
                        cur = new BMNull(i, j);
                        break;
                } // switch

                map[i][j] = cur;
            } // for j
        } // for i
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public BMWidget getWidgetAt(int i,int j) {
        return map[i][j];
    }
}
