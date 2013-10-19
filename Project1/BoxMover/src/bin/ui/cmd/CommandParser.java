package bin.ui.cmd;

import bin.Env;
import bin.exp.ArgumentError;
import bin.exp.CommandNotFound;
import bin.exp.MatchMoreThanOneCommand;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class CommandParser {
    CommandTrie trie = new CommandTrie();
    private CommandParser() {
        for(int i = 0;i < Env.COMMANDS.length;i ++) {
            String cmd_name = (String)Env.COMMANDS[i][0];
            int cmd_type = (Integer)Env.COMMANDS[i][1];

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
            case Env.CMD_TYPE_NO_ARG:
                arg = filterNone(arg);
                break;
            case Env.CMD_TYPE_INT_REQ_ARG:
                arg = filterIntegerRequire(arg);
                break;
            case Env.CMD_TYPE_INT_OPT_ARG:
                arg = filterIntegerOptional(arg);
                break;
        }

        if(arg != null) {
            return new Command(trie.getCommandFullName(), Integer.parseInt((String)arg));
        } else {
            return new Command(trie.getCommandFullName());
        }
    }

    static private Object filterNone(Object arg)
    throws ArgumentError {
        if(arg != null) {
            throw new ArgumentError("This Command takes no argument!");
        }

        return null;
    }

    static private Object filterIntegerRequire(Object arg)
    throws ArgumentError {
         try {
            Integer.parseInt((String)arg);
         } catch (Exception e) {
             throw new ArgumentError("This Command require a integer argument!");
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
