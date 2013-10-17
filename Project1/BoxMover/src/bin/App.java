package bin;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */

public class App {
    int cur_level;

    private void printWelcome() {
        System.out.println("Hello World! This is the welcome info!");
    }

    private void chooseLevel() {
        cur_level = 1;
    }

    private void printVictory() {

    }

    private void printFailed() {

    }

    private boolean playLevel() {
        return true;
    }

    private boolean isFinished() {
        return false;
    }

    private void mainLoop() {
        printWelcome();

        chooseLevel();

        while(!isFinished()) {
            if(playLevel()) {
                cur_level += 1;
                printVictory();
            } else {
                printFailed();
            }
        }
    }

    public int run() {
        try {
            mainLoop();
        } catch (Exception e) {
            return -1;
        }
        return 0;
    }
}
