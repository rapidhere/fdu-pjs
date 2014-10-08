package rapid;

import rapid.ctrl.GameCommandEvent;
import rapid.ctrl.GameCommandListener;
import rapid.ui.*;
import rapid.widget.BMMovable;
import rapid.widget.RankList;
import rapid.widget.User;

import javax.swing.*;
import java.io.File;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class : App
 * Version : 0.1
 * Usage : The logic part of application
 */
public class App {
    private Frame win;  // The frame  of the game
    private GameCommandAdapter gl = new GameCommandAdapter(); // Game event listener

    // The state of game
    public static final int
        GAME_STATE_START_MENU = 0,
        GAME_STATE_GAME = 1,
        GAME_STATE_AUTHOR = 2,
        GAME_STATE_HELP = 3,
        GAME_STATE_RANK = 4,
        GAME_STATE_LEVEL_VICTORY = 5,
        GAME_STATE_CHOOSE = 6;

    // The current user of game
    private User currentUser = null;

    // Constructor
    public App() {
        win = new Frame(gl);
    }

    // start application
    public void run() {
        setGameState(GAME_STATE_START_MENU);
    }

    // set the current state of game
    private void setGameState(int st) {
        switch (st) {
            default: // default to GAME_STATE_START_MENU
            case GAME_STATE_START_MENU:
                currentUser = null;
                win.setGameState(new StartPanel(), gl, null);
                break;
            case GAME_STATE_GAME:
                win.setGameState(new GamePanel(currentUser), gl, new MenuBar(gl));
                break;
            case GAME_STATE_AUTHOR:
                win.setGameState(new AuthorPanel(), gl, null);
                break;
            case GAME_STATE_HELP:
                win.setGameState(new HelpPanel(), gl, null);
                break;
            case GAME_STATE_RANK:
                win.setGameState(new RankPanel(), gl, null);
                break;
            case GAME_STATE_LEVEL_VICTORY:
                win.setGameState(new LevelVictoryPanel(currentUser), gl, null);
                break;
            case GAME_STATE_CHOOSE:
                win.setGameState(new ChoosePanel(currentUser), gl, null);
                break;
        }
    }

    // The listener of game
    // This is the true controller of application
    public class GameCommandAdapter implements GameCommandListener {
        // Exit game, do some extra saving work
        public void onExit(GameCommandEvent e) {
            if(currentUser != null) {
                currentUser.save();
            }

            RankList.get().save();

            System.exit(0);
        }

        // Create a new user
        public void onNewUser(GameCommandEvent e) {
            String ret = JOptionPane.showInputDialog(win, Env.NEW_USER_TITLE);
            if(ret != null) { // ENTER
                // Check user name
                if(Env.NEW_USER_NAME_CHECKER.matcher(ret).matches()) {
                    File f = new File(Env.USR_DIRECTORY + ret);
                    if(f.exists()) { // user existed
                        JOptionPane.showMessageDialog(win, Env.NEW_USER_USER_NAME_EXISTS, "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    currentUser = User.newUser(ret);
                    setGameState(GAME_STATE_RANK);
                } else { // Illegal user name
                    JOptionPane.showMessageDialog(win, Env.NEW_USER_WRONG_NAME_MSG, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        // Continue a game
        public void onOldUser(GameCommandEvent e) {
            JFileChooser jfc = new JFileChooser(Env.USR_DIRECTORY);
            jfc.setDialogTitle("Choose User");
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

            // Enter
            if(jfc.showOpenDialog(win) == JFileChooser.APPROVE_OPTION) {
                currentUser = User.getUser(jfc.getSelectedFile().getName());
                setGameState(GAME_STATE_GAME);
            }
        }

        // check if won or not
        private void check(GameCommandEvent e) {
            if(currentUser.getMap().isWon()) {
                onLevelVictory(e);
            }

            if(currentUser.getMap().isFailed()) {
                onLevelFailed(e);
            }
        }

        // move up
        public void onKeyUp(GameCommandEvent e) {
            currentUser.getMap().movePerson(BMMovable.DIRECTION_UP);
            check(e);
        }

        // move down
        public void onKeyDown(GameCommandEvent e) {
            currentUser.getMap().movePerson(BMMovable.DIRECTION_DOWN);
            check(e);
        }

        // move right
        public void onKeyRight(GameCommandEvent e) {
            currentUser.getMap().movePerson(BMMovable.DIRECTION_RIGHT);
            check(e);
        }

        // move left
        public void onKeyLeft(GameCommandEvent e) {
            currentUser.getMap().movePerson(BMMovable.DIRECTION_LEFT);
            check(e);
        }

        // back step
        public void onBackStep(GameCommandEvent e) {
            currentUser.getMap().moveBack((Integer)e.cmdArgs[0]);
        }

        // level won
        public void onLevelVictory(GameCommandEvent e) {
            currentUser.incScore(currentUser.getMap().getScore());
            currentUser.incHighLv();
            setGameState(GAME_STATE_LEVEL_VICTORY);
        }

        // level failed: do nothing
        public void onLevelFailed(GameCommandEvent e) {
            // Do nothing
        }

        // show author panel
        public void onShowAbout(GameCommandEvent e) {
            setGameState(GAME_STATE_AUTHOR);
        }

        // show help panel
        public void onShowHelp(GameCommandEvent e) {
            setGameState(GAME_STATE_HELP);
        }

        // show choose level panel
        public void onChooseLevel(GameCommandEvent e) {
            setGameState(GAME_STATE_CHOOSE);
        }

        // restart level
        public void onRestartLevel(GameCommandEvent e) {
            try {
                currentUser.getMap().loadLevel(currentUser.getMap().getLevel());
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }

        // save level
        public void onSaveLevel(GameCommandEvent e) {
            currentUser.save();
        }

        // show rank panel
        public void onShowRank(GameCommandEvent e) {
            setGameState(GAME_STATE_RANK);
        }

        // move back to level
        public void onBackToLevel(GameCommandEvent e) {
            setGameState(GAME_STATE_GAME);
        }
    }
}
