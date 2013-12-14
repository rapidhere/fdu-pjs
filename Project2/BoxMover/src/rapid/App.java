package rapid;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
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
 * Class :
 * Version :
 * Usage :
 */
public class App {
    private Frame win;
    private GameCommandAdapter gl = new GameCommandAdapter();
    public static final int
        GAME_STATE_START_MENU = 0,
        GAME_STATE_GAME = 1,
        GAME_STATE_AUTHOR = 2,
        GAME_STATE_HELP = 3,
        GAME_STATE_RANK = 4,
        GAME_STATE_LEVEL_VICTORY = 5,
        GAME_STATE_CHOOSE = 6;

    private User currentUser = null;

    public App() {
        win = new Frame(gl);
    }

    public void run() {
        setGameState(GAME_STATE_START_MENU);
    }

    private void setGameState(int st) {
        switch (st) {
            default:
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

    public class GameCommandAdapter implements GameCommandListener {
        public void onExit(GameCommandEvent e) {
            if(currentUser != null) {
                currentUser.save();
            }

            RankList.get().save();

            System.exit(0);
        }

        public void onNewUser(GameCommandEvent e) {
            String ret = JOptionPane.showInputDialog(win, Env.NEW_USER_TITLE);
            if(ret != null) {
                if(Env.NEW_USER_NAME_CHECKER.matcher(ret).matches()) {
                    File f = new File(Env.USR_DIRECTORY + ret);
                    if(f.exists()) {
                        JOptionPane.showMessageDialog(win, Env.NEW_USER_USER_NAME_EXISTS, "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    currentUser = User.newUser(ret);
                    setGameState(GAME_STATE_GAME);
                } else {
                    JOptionPane.showMessageDialog(win, Env.NEW_USER_WRONG_NAME_MSG, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        public void onOldUser(GameCommandEvent e) {
            JFileChooser jfc = new JFileChooser(Env.USR_DIRECTORY);
            jfc.setDialogTitle("Choose User");
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if(jfc.showOpenDialog(win) == JFileChooser.APPROVE_OPTION) {
                currentUser = User.getUser(jfc.getSelectedFile().getName());
                setGameState(GAME_STATE_GAME);
            }
        }

        private void check(GameCommandEvent e) {
            if(currentUser.getMap().isWon()) {
                onLevelVictory(e);
            }

            if(currentUser.getMap().isFailed()) {
                onLevelFailed(e);
            }
        }

        public void onKeyUp(GameCommandEvent e) {
            currentUser.getMap().movePerson(BMMovable.DIRECTION_UP);
            check(e);
        }

        public void onKeyDown(GameCommandEvent e) {
            currentUser.getMap().movePerson(BMMovable.DIRECTION_DOWN);
            check(e);
        }

        public void onKeyRight(GameCommandEvent e) {
            currentUser.getMap().movePerson(BMMovable.DIRECTION_RIGHT);
            check(e);
        }

        public void onKeyLeft(GameCommandEvent e) {
            currentUser.getMap().movePerson(BMMovable.DIRECTION_LEFT);
            check(e);
        }

        public void onBackStep(GameCommandEvent e) {
            currentUser.getMap().moveBack((Integer)e.cmdArgs[0]);
        }

        public void onLevelVictory(GameCommandEvent e) {
            currentUser.incScore(currentUser.getMap().getScore());
            currentUser.incHighLv();
            setGameState(GAME_STATE_LEVEL_VICTORY);
        }

        public void onLevelFailed(GameCommandEvent e) {
            // Do nothing
        }

        public void onGameVictory(GameCommandEvent e) {
            // Do nothing
        }

        public void onShowAbout(GameCommandEvent e) {
            setGameState(GAME_STATE_AUTHOR);
        }

        public void onShowHelp(GameCommandEvent e) {
            setGameState(GAME_STATE_HELP);
        }

        public void onChooseLevel(GameCommandEvent e) {
            setGameState(GAME_STATE_CHOOSE);
        }

        public void onRestartLevel(GameCommandEvent e) {
            try {
                currentUser.getMap().loadLevel(currentUser.getMap().getLevel());
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }

        public void onSaveLevel(GameCommandEvent e) {
            currentUser.save();
        }

        public void onShowRank(GameCommandEvent e) {
            setGameState(GAME_STATE_RANK);
        }

        public void onBackToLevel(GameCommandEvent e) {
            setGameState(GAME_STATE_GAME);
        }
    }
}
