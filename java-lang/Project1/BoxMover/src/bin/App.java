package bin;

import bin.exp.LevelNotFound;
import bin.io.IOHelper;
import bin.ui.generic.*;
import bin.ui.textbaseui.TextBaseUIFactory;
import bin.widget.BMMap;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class : App
 * Version : ver 0.1
 * Usage : The Application Object. use Application.run() to start the application
 */

public class App {
    /** All UserInterfaces defined here
     * uifac is used to generate UIWidgets
     * These are :
     *      menu_ui, used to draw menu
     *      map_ui, used to display map and stage info
     *      info_ui, draw some text on the screen
     * For More info, please refer to the Introduction to UserInterface Module in Developer Manual
    */
    private GenericUIFactory uifac = new TextBaseUIFactory();
    private GenericMenuUI menu_ui = uifac.getMenuUI();
    private GenericMapUI map_ui = uifac.getMapUI();
    private GenericInfoUI info_ui = uifac.getInfoUI();

    /** The Current State of Application
     * Indicate the which Function to run
    */
    static private final int
        RUN_STATE_START_MENU = 0,       // Now is at the StartMenu, Game is not started yet
        RUN_STATE_START_GAME = 1,       // We start to play the game, But the Game is not started yet, we're doing some init job
        RUN_STATE_LEVEL_START = 2,      // Start A Level, This could be a new level, or restore from a saved stage
        RUN_STATE_LEVEL_VICTORY = 3,    // We Won Current level
        RUN_STATE_LEVEL_FAILED = 4,     // Current Level failed
        RUN_STATE_VICTORY = 5,          // Game victory, we've done the last stage
        RUN_STATE_LOAD_SAVE = 6,        // We are trying to load a saved stage
        RUN_STATE_EXIT = 7;             // Time to Exit

    static private BMMap curmap;        // Current stage we're playing

    /** State Functions
     * When Application is at specified state indicated by RUN_STATE_* constants,
     * specified State Function will run
     * These Functions are defined as follow:
     *      RUN_STATE_START_MENU        startMenu
     *      RUN_STATE_START_GAME        startPlay
     *      RUN_STATE_LEVEL_START       play
     *      RUN_STATE_LEVEL_VICTORY     levelVictory
     *      RUN_STATE_LEVEL_FAILED      levelFailed
     *      RUN_STATE_VICTORY           victory
     *      RUN_STATE_LOAD_SAVE         loadSave
     *      RUN_STATE_EXIT              <null>
     * For full information please refer to Application Section in Developer Manual
     * these functions shouldn't call directly
     * they're called by runState()
    */

    /* BEGIN OF RUN_STATE_FUNCTIONS */

    /* The very beginning ui of the application */
    private int startMenu() {
        // Menu Items
        String[] items= {
            "Start",    // start play
            "Load",     // load saved
            "Exit",     // exit application
        };

        while (true) {
            // Setup MenuUI
            menu_ui.setMenu(items, "Box Mover v 0.1");
            menu_ui.draw();

            // Get and Handle Choice
            int index = menu_ui.getChoice();

            switch (index) {
                case 0: return RUN_STATE_START_GAME;
                case 1: return RUN_STATE_LOAD_SAVE;
                case 2: return RUN_STATE_EXIT;
            }
        }
    }

    /* play a specified level or stage */
    private int play()
    throws LevelNotFound {
        // Show the Level Info
        info_ui.setTitle("Level " + curmap.getLevel());
        info_ui.setContent("Let's push!!!\n");
        info_ui.draw();

        // Current map
        BMMap map = curmap;

        while (true) {
            // We Check Victory first
            // Because some times the Victory stage is still a Death Stage
            if(map.isWon()) {
                return RUN_STATE_LEVEL_VICTORY;
            } else if(map.isFailed()) {
                return RUN_STATE_LEVEL_FAILED;
            }

            // Setup MAP_UI and draw it
            map_ui.setMap(map);
            map_ui.draw();

            // Get Command from map_ui
            Command cmd = map_ui.getCommand();

            // Set Info to null
            // Some info will add to Map_ui and will display at next step
            map_ui.setInfo(null);

            // Handle Commands
            switch (cmd.getCommandId()) {
                case Env.CMD_EXIT:  // exit application
                    return RUN_STATE_EXIT;
                case Env.CMD_CHOSE: // Chose Level
                    curmap = new BMMap();
                    curmap.loadLevel(cmd.getArgument());
                    return RUN_STATE_LEVEL_START;
                case Env.CMD_BACK:  // Back steps
                    if(cmd.hasArgument()) { // if specified steps
                        map.moveBack(cmd.getArgument());
                    } else { // default is back one step
                        map.moveBack(1);
                    }
                    break;
                case Env.CMD_RESTART:   // Restart Current Level
                    curmap = new BMMap();
                    curmap.loadLevel(map.getLevel());
                    return RUN_STATE_LEVEL_START;
                case Env.CMD_SAVE:  // Save Game
                    map.saveGame();
                    map_ui.setInfo("Game saved as " + Env.getSaveFileName() + "\n");
                    break;
                case Env.CMD_BACKMENU: // Back to main menu
                    return RUN_STATE_START_MENU;
                case Env.CMD_HELP: // Help
                    info_ui.setTitle("Brief Help Manual");
                    info_ui.setContent(
                        "Commands:\n" +
                        "   w               move up\n" +
                        "   s               move down\n" +
                        "   a               move left\n" +
                        "   d               move right\n" +
                        "   e[xit]          exit application\n" +
                        "   r[estart]       restart level\n" +
                        "   b[ack] [steps]  back steps\n" +
                        "   c[hose] <level> chose level\n" +
                        "   g[save]         save stage\n" +
                        "   m[ain]          back to main menu\n" +
                        "   h[elp]          show the help info\n" +
                        "For more information please read the User Manual"
                    );

                    info_ui.draw();
                    break;
                case Env.CMD_UP: // move up
                    map.movePerson(Env.DIRECTION_UP);
                    break;
                case Env.CMD_DOWN: // move down
                    map.movePerson(Env.DIRECTION_DOWN);
                    break;
                case Env.CMD_LEFT: // move left
                    map.movePerson(Env.DIRECTION_LEFT);
                    break;
                case Env.CMD_RIGHT: // move right
                    map.movePerson(Env.DIRECTION_RIGHT);
                    break;
            }
        }
    }

    /* preparing to start play */
    private int startPlay()
    throws LevelNotFound {
        // Set map to level 1
        curmap = new BMMap();
        curmap.loadLevel(1);
        return RUN_STATE_LEVEL_START;
    }

    /* current level failed */
    private int levelFailed()
    throws LevelNotFound {
        info_ui.setTitle("Lose ~");
        info_ui.setContent("You failed at Level " + curmap.getLevel() + "\nPlease Try again :(\n");
        info_ui.draw();

        int cur_lv = curmap.getLevel();
        curmap = new BMMap();
        curmap.loadLevel(cur_lv);
        return RUN_STATE_LEVEL_START;
    }

    /* current level won */
    private int levelVictory()
    throws LevelNotFound {
        if(curmap.getLevel() == Env.MAX_LEVEL) { // Done last stage, game victory
            return RUN_STATE_VICTORY;
        }
        // Set up info_ui
        info_ui.setTitle("WoHoo ~");
        info_ui.setContent("You've finish " + curmap.getLevel() + "\nNow is next level :)\n");
        info_ui.draw();

        // Step to next level
        int lv = curmap.getLevel() + 1;
        curmap.loadLevel(lv);
        return RUN_STATE_LEVEL_START;
    }

    /* game won */
    private int victory() {
        info_ui.setTitle("Awesome!");
        info_ui.setContent("You've finish the hardest level!\nCongratulations!\n");
        info_ui.draw();
        return RUN_STATE_START_MENU;
    }

    /* load saved stage */
    private int loadSave() {
        // Get Saved Game List
        String[] list = BMMap.getSaveList();

        // No Saved Games
        if(list.length == 0) {
            info_ui.setTitle("Error");
            info_ui.setContent("No saved game(s) found!");
            info_ui.draw();
            return RUN_STATE_START_MENU;
        }

        // Set up menu ui
        menu_ui.setMenu(list, "Chose saved game(s)");
        menu_ui.draw();

        // get choice
        String ch = list[menu_ui.getChoice()];

        // load stage
        curmap = BMMap.readGame(ch);
        return RUN_STATE_LEVEL_START;
    }
    /* END OF RUN_STATE_FUNCTIONS */


    /**
     * call RunStateFunctions specified by stat
     *
     * @param stat the state of application , one of the RUN_STATE_*
     * @return the next State of the application should be
     * @throws LevelNotFound
     */
    private int runState(int stat)
    throws LevelNotFound {
        switch (stat) {
            case RUN_STATE_START_MENU: return startMenu();
            case RUN_STATE_START_GAME: return startPlay();
            case RUN_STATE_LEVEL_START: return play();
            case RUN_STATE_VICTORY: return victory();
            case RUN_STATE_LEVEL_VICTORY: return levelVictory();
            case RUN_STATE_LEVEL_FAILED: return levelFailed();
            case RUN_STATE_LOAD_SAVE: return loadSave();
        }
        return RUN_STATE_EXIT;
    }

    /**
     * The mainLoop of Application
     * @throws LevelNotFound
     */
    private void mainLoop()
    throws LevelNotFound {
        int stat = RUN_STATE_START_MENU;
        while (stat != RUN_STATE_EXIT) {
            stat = runState(stat);
        }
    }

    /**
     * The public entry
     * @return Application Return code
     */
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
