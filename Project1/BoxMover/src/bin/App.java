package bin;

import bin.Exception.LevelNotFound;
import bin.IOHelper.IOHelper;
import bin.Widget.BMMap;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */

public class App {
    int cur_level;
    BMMap map;
    boolean exit_flag = false;

    private void printWelcome() {
        System.out.println("Hello World! This is the welcome info!");
    }

    private void chooseLevel() {
        cur_level = 3;
    }

    private void printVictory() {
        System.out.println("Win " + cur_level);
    }

    private void printFailed() {
        System.out.println("Lose " + cur_level);
    }

    private void printAllKill() {

    }

    private boolean playLevel()
    throws LevelNotFound {
        map.loadMap(cur_level);

        while(true) {
            if(map.isWon()) {
                return true;
            } else if(map.isFailed()) {
                return false;
            }

            map.draw();
            switch (IOHelper.getLowerChar()) {
                case Env.KEY_UP:
                    map.movePerson(Env.DIRECTION_UP);
                    break;
                case Env.KEY_DOWN:
                    map.movePerson(Env.DIRECTION_DOWN);
                    break;
                case Env.KEY_LEFT:
                    map.movePerson(Env.DIRECTION_LEFT);
                    break;
                case Env.KEY_RIGHT:
                    map.movePerson(Env.DIRECTION_RIGHT);
                    break;
                case Env.KEY_EXIT:
                    exitLoop();
                    return true;
            }
        }
    }


    private void exitLoop() {
        System.exit(0);
    }

    private void mainLoop()
    throws LevelNotFound {
        printWelcome();

        chooseLevel();
        map = new BMMap();

        while(true) {
            boolean result = playLevel();
            if(exit_flag) {
                break;
            }

            if(result) {
                cur_level += 1;
                printVictory();

                if(cur_level > Env.MAX_LEVEL) {
                    printAllKill();
                    return;
                }
            } else {
                printFailed();
                return;
            }
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
