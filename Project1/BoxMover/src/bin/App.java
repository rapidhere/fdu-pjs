package bin;

import bin.exp.LevelNotFound;
import bin.io.IOHelper;
import bin.ui.generic.*;
import bin.ui.textbaseui.cmd.CommandParser;
import bin.ui.textbaseui.TextBaseUIFactory;
import bin.widget.BMMap;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class : App
 * Version : ver 0.1
 * Usage : The main application of the game
 */

public class App {
    GenericUIFactory uifac = new TextBaseUIFactory();
    GenericMenuUI menu_ui = uifac.getMenuUI();
    GenericMapUI map_ui = uifac.getMapUI();
    GenericInfoUI info_ui = uifac.getInfoUI();

    static private final int
        RUN_STATE_START_MENU = 0,
        RUN_STATE_START_GAME = 1,
        RUN_STATE_LEVEL_START = 2,
        RUN_STATE_LEVEL_VICTORY = 3,
        RUN_STATE_LEVEL_FAILED = 4,
        RUN_STATE_VICTORY = 5,
        RUN_STATE_EXIT = 6;

    static private int curlv;

    private int startMenu() {
        String[] items= {
            "Start",
            "Exit",
        };

        while (true) {
            menu_ui.setMenu(items, "Box Mover v 0.1");
            menu_ui.draw();
            int index = menu_ui.getChoice();

            switch (index) {
                case 0: return RUN_STATE_START_GAME;
                case 1: return RUN_STATE_EXIT;
            }
        }
    }

    private int playLevel()
    throws LevelNotFound {
        info_ui.setTitle("Level " + curlv);
        info_ui.setContent("Let's push!!!\n");
        info_ui.draw();

        BMMap map = new BMMap();

        map.loadMap(curlv);

        while (true) {
            if(map.isWon()) {
                return RUN_STATE_LEVEL_VICTORY;
            } else if(map.isFailed()) {
                return RUN_STATE_LEVEL_FAILED;
            }

            map_ui.setMap(map);
            map_ui.setCurStep(map.getCurrentStep());
            map_ui.draw();

            Command cmd = map_ui.getCommand();
            map_ui.setInfo(null);
            switch (cmd.getCommandId()) {
                case Env.CMD_EXIT:
                    return RUN_STATE_EXIT;
                case Env.CMD_CHOSE:
                    curlv = cmd.getArgument();
                    return RUN_STATE_LEVEL_START;
                case Env.CMD_BACK:
                    if(cmd.hasArgument()) {
                        map.moveBack(cmd.getArgument());
                    } else {
                        map.moveBack(1);
                    }
                    break;
                case Env.CMD_RESTART:
                    return RUN_STATE_LEVEL_START;
                case Env.CMD_HELP:
                    break;
                case Env.CMD_UP:
                    map.movePerson(Env.DIRECTION_UP);
                    break;
                case Env.CMD_DOWN:
                    map.movePerson(Env.DIRECTION_DOWN);
                    break;
                case Env.CMD_LEFT:
                    map.movePerson(Env.DIRECTION_LEFT);
                    break;
                case Env.CMD_RIGHT:
                    map.movePerson(Env.DIRECTION_RIGHT);
                    break;
            }
        }
    }

    private int startPlay() {
        curlv = 1;
        return RUN_STATE_LEVEL_START;
    }

    private int levelFailed() {
        info_ui.setTitle("Lose ~");
        info_ui.setContent("You failed at Level " + curlv + "\nPlease Try again :(\n");
        info_ui.draw();
        return RUN_STATE_LEVEL_START;
    }

    private int levelVictory() {
        if(curlv == Env.MAX_LEVEL) {
            return RUN_STATE_VICTORY;
        }
        info_ui.setTitle("WoHoo ~");
        info_ui.setContent("You've finish " + curlv + "\nNow is next level :)\n");
        info_ui.draw();
        curlv ++;
        return RUN_STATE_LEVEL_START;
    }

    private int victory() {
        info_ui.setTitle("Awesome!");
        info_ui.setContent("You've finish the hardest level!\nCongratulations!\n");
        info_ui.draw();
        return RUN_STATE_START_MENU;
    }

    private int runState(int stat)
    throws LevelNotFound {
        switch (stat) {
            case RUN_STATE_START_MENU: return startMenu();
            case RUN_STATE_START_GAME: return startPlay();
            case RUN_STATE_LEVEL_START: return playLevel();
            case RUN_STATE_VICTORY: return victory();
            case RUN_STATE_LEVEL_VICTORY: return levelVictory();
            case RUN_STATE_LEVEL_FAILED: return levelFailed();
        }
        return RUN_STATE_EXIT;
    }

    private void mainLoop()
    throws LevelNotFound {
        int stat = RUN_STATE_START_MENU;
        while (stat != RUN_STATE_EXIT) {
            stat = runState(stat);
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
