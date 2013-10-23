package bin.ui.textbaseui.cmd;

import bin.Env;
import bin.exp.ArgumentError;
import bin.exp.CommandNotFound;
import bin.exp.MatchMoreThanOneCommand;
import bin.ui.generic.Command;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class CommandParser {
    CommandTrie trie = new CommandTrie();
    public static final int
        CMD_TYPE_NO_ARG = 0,
        CMD_TYPE_INT_OPT_ARG = 1,
        CMD_TYPE_INT_RANGE_ARG = 2;

    public static final String
        CMD_UP = "w",
        CMD_DOWN = "s",
        CMD_RIGHT = "d",
        CMD_LEFT = "a",
        CMD_EXIT = "exit",
        CMD_RESTART = "restart",
        CMD_BACK = "back",
        CMD_CHOSE = "chose",
        CMD_HELP = "help";

    public static final Object[][] COMMANDS = {
        {CMD_UP,        CMD_TYPE_INT_OPT_ARG},
        {CMD_DOWN,      CMD_TYPE_INT_OPT_ARG},
        {CMD_RIGHT,     CMD_TYPE_INT_OPT_ARG},
        {CMD_LEFT,      CMD_TYPE_INT_OPT_ARG},
        {CMD_EXIT,      CMD_TYPE_NO_ARG},
        {CMD_RESTART,   CMD_TYPE_NO_ARG},
        {CMD_BACK,      CMD_TYPE_INT_OPT_ARG},
        {CMD_CHOSE,     CMD_TYPE_INT_RANGE_ARG},
        {CMD_HELP,      CMD_TYPE_NO_ARG},
    };

    private CommandParser() {
        for(int i = 0;i < COMMANDS.length;i ++) {
            String cmd_name = (String)COMMANDS[i][0];
            int cmd_type = (Integer)COMMANDS[i][1];

            trie.registerCommand(cmd_name, cmd_type);
        }

        trie.buildTrie();
    }

    public Command parseCommandLine(String cmd_line)
    throws CommandNotFound, ArgumentError, MatchMoreThanOneCommand {
        int space_pos = cmd_line.indexOf(' ');

        String cmd_name;
        Object arg;
        if(space_pos == -1) {
            cmd_name = cmd_line;
            arg = null;
        } else {
            arg = cmd_line.substring(space_pos + 1);
            cmd_name = cmd_line.substring(0, space_pos);
        }

        trie.parseCommandName(cmd_name);
        switch (trie.getCommandArgumentType()) {
            case CMD_TYPE_NO_ARG:
                arg = filterNone(arg);
                break;
            case CMD_TYPE_INT_RANGE_ARG:
                arg = filterIntegerRange(arg);
                break;
            case CMD_TYPE_INT_OPT_ARG:
                arg = filterIntegerOptional(arg);
                break;
        }

        if(arg != null) {
            return new Command(toCommandId(trie.getCommandFullName()), Integer.parseInt((String)arg));
        } else {
            return new Command(toCommandId(trie.getCommandFullName()));
        }
    }

    static private final Object[][] NAME_TO_ID_MAP = {
        {CMD_UP,        Env.CMD_UP},
        {CMD_DOWN,      Env.CMD_DOWN},
        {CMD_RIGHT,     Env.CMD_RIGHT},
        {CMD_LEFT,      Env.CMD_LEFT},
        {CMD_RESTART,   Env.CMD_RESTART},
        {CMD_BACK,      Env.CMD_BACK},
        {CMD_CHOSE,     Env.CMD_CHOSE},
        {CMD_HELP,      Env.CMD_HELP},
        {CMD_EXIT,      Env.CMD_EXIT},
    };
    static private int toCommandId(String cmd_name) {
        for(int i = 0;i < NAME_TO_ID_MAP.length;i ++) {
            if(cmd_name.compareTo((String)NAME_TO_ID_MAP[i][0]) == 0) {
                return (Integer)NAME_TO_ID_MAP[i][1];
            }
        }
        return -1;
    }

    static private Object filterNone(Object arg)
    throws ArgumentError {
        if(arg != null) {
            throw new ArgumentError("This Command takes no argument!");
        }

        return null;
    }

    static private Object filterIntegerRange(Object arg)
    throws ArgumentError {
        int a;
        try {
            a = Integer.parseInt((String)arg);
        } catch (Exception e) {
            throw new ArgumentError("This Command require a integer argument!");
        }

        if(a < 1 || a > Env.MAX_LEVEL) {
            throw new ArgumentError("Please give a integer between 1 and " + Env.MAX_LEVEL + "!");
        }

        return arg;
    }

    static private Object filterIntegerOptional(Object arg)
    throws ArgumentError {
        if(arg == null) {
            return arg;
        }

        try {
            Integer.parseInt((String)arg);
        } catch (Exception e) {
            throw new ArgumentError("This Command require a integer argument or none!");
        }

        return arg;
    }

    static private CommandParser _instance = null;

    static public CommandParser getCommandParser() {
        if(_instance == null) {
            _instance = new CommandParser();
        }

        return _instance;
    }
}
