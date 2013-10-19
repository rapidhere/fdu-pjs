package bin;

import bin.exp.ArgumentError;
import bin.exp.CommandNotFound;
import bin.exp.LevelNotFound;
import bin.exp.MatchMoreThanOneCommand;
import bin.io.IOHelper;
import bin.ui.cmd.CommandParser;
import bin.ui.cmd.Command;
import bin.ui.generic.GenericMapUI;
import bin.ui.generic.GenericMenuUI;
import bin.ui.generic.GenericUIFactory;
import bin.ui.textbaseui.TextBaseUIFactory;
import bin.widget.BMMap;

import java.util.TooManyListenersException;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */

public class App {
    GenericUIFactory uifac = new TextBaseUIFactory();
    GenericMenuUI menu_ui = uifac.getMenuUI();
    GenericMapUI map_ui = uifac.getMapUI();

    static private final int
        STATE_EXIT = 0,
        STATE_START = 1,
        STATE_RESTART = 2,
        STATE_CHOSE = 3,
        STATE_VICTORY = 4,
        STATE_FAILED = 5;

    static private int chose_lv;

    private int startMenu() {
        String[] items= {
            "Start",
            "Exit",
        };

        while (true) {
            menu_ui.setMenu(items, "Box Mover v 0.1");
            menu_ui.draw();
            int index;
            try {
                index = Integer.parseInt(menu_ui.getCommand());
            } catch (NumberFormatException e) {
                continue;
            }

            switch (index) {
                case 0: return STATE_START;
                case 1: return STATE_EXIT;
            }
        }
    }

    private int playLevel(int level)
    throws LevelNotFound {
        BMMap map = new BMMap();
        CommandParser cp = CommandParser.getCommandParser();

        map.loadMap(level);

        while (true) {
            if(map.isWon()) {

            } else if(map.isFailed()) {

            }

            map_ui.setMap(map);
            map_ui.draw();

            Command cmd;
            while (true) {
                try {
                    cmd = cp.parseCommandLine(map_ui.getCommand());
                    break;
                } catch (CommandNotFound cnf) {
                    map_ui.getIOHandler().putError("Cannot found cmd: "+ cnf.getCommandName());
                } catch (ArgumentError ae) {
                    map_ui.getIOHandler().putError(ae.getExString());
                } catch (MatchMoreThanOneCommand m) {
                    String es = "Command " + m.getCmdName() + " Matched more than one command:\n";
                    for(int i = 0;i < m.getMatchedSize();i ++)
                        es += "\t" + m.get(i);
                    map_ui.getIOHandler().putError(es);
                }
            }

            if(cmd.getCommandName().compareTo(Env.CMD_UP) == 0) {
                map.movePerson(Env.DIRECTION_UP);
            } else if(cmd.getCommandName().compareTo(Env.CMD_DOWN) == 0) {
                map.movePerson(Env.DIRECTION_DOWN);
            } else if(cmd.getCommandName().compareTo(Env.CMD_LEFT) == 0) {
                map.movePerson(Env.DIRECTION_LEFT);
            } else if(cmd.getCommandName().compareTo(Env.CMD_RIGHT) == 0) {
                map.movePerson(Env.DIRECTION_RIGHT);
            } else if(cmd.getCommandName().compareTo(Env.CMD_RESTART) == 0) {
                return STATE_RESTART;
            } else if(cmd.getCommandName().compareTo(Env.CMD_BACK) == 0) {

            } else if(cmd.getCommandName().compareTo(Env.CMD_CHOSE) == 0) {
                chose_lv = cmd.getArgument();
                return STATE_CHOSE;
            } else if(cmd.getCommandName() == Env.CMD_EXIT) {
                return STATE_EXIT;
            }

        }
    }
    private int startPlay()
    throws LevelNotFound {
        int level = 1;
        while (true) {
            int stat = playLevel(level);
        }
    }

    private void mainLoop()
    throws LevelNotFound {
        while (true) {
            int stat = startMenu();

            switch (stat) {
                case STATE_EXIT:
                    return;
                case STATE_START:
                    stat = startPlay();
                    break;
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
