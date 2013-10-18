package bin;

import bin.exp.LevelNotFound;
import bin.io.IOHelper;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */

public class App {
    private void mainLoop()
    throws LevelNotFound {
        while(true) {

        }
    }

    public int run() {
        try {
            mainLoop();
        } catch (LevelNotFound lnf) {
            IOHelper.putError("Cannot find level " + lnf.getLevel());
            return -1;
        }
        return 0;
    }
}
