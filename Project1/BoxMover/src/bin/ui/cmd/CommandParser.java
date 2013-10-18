package bin.ui.cmd;

import bin.Env;
import bin.exp.ArgumentError;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class CommandParser {
    private class CommandCharNode{
        int child[] = new int[26];
        int cmd_id;

        boolean end_flag = false;

        CommandCharNode() {
            for(int i = 0;i < child.length;i ++)
                child[i] = -1;

            cmd_id = -1;
        }
    }

    private CommandCharNode[] cmd_trie = new CommandCharNode[1000];
    int ncmd_trie = 0;
    private int[] cmds = new int[100];
    private int ncmd = 0;

    private CommandParser() {
        for(int i = 0;i < Env.COMMANDS.length;i ++) {
            Object[] cmd = Env.COMMANDS[i];
            registerCommand((String)cmd[0], (Integer)cmd[1]);
        }
    }

    public void registerCommand(String cmd_name,int CMD_TYPE) {
        // TODO
    }

    public Command parseArgument(String cmd_line)
    throws ArgumentError {
        int space_pos = cmd_line.indexOf(' ');

        Object arg;
        String cmd_name = cmd_line.substring(0, space_pos);
        if(space_pos == -1) {
            arg = "";
        } else {
            arg = cmd_line.substring(space_pos + 1);
        }

        int ptr = 0;
        int cmd_id = -1;

        for(int i = 0;i < cmd_name.length();i ++) {
            int ind = cmd_name.charAt(i) - 'a';
            CommandCharNode cur = cmd_trie[ptr];

            if(cur.child[ind] == -1) {
                break;
            }

            ptr = cur.child[ind];
            if(cmd_trie[ptr].cmd_id != -1) {
                cmd_id = cmd_trie[ptr].cmd_id;
            }
        }

        // TODO

        if(arg == null) {
            return new Command(cmd_name);
        } else {
            return new Command(cmd_name, (Integer)arg);
        }

    }

    private Object filterNone(String a)
    throws ArgumentError {
        if(a != "") {
            throw new ArgumentError("Needs no Argument!");
        }
        return null;
    }

    private Object filterIntegerRequire(String a)
    throws ArgumentError {
        try {
            Integer.parseInt(a);
        } catch (NumberFormatException e) {
            throw new ArgumentError("Must input a integer!");
        }

        return a;
    }

    private Object filterIntegerOptional(String a)
    throws ArgumentError {
        if(a == "") {
            return null;
        }
        try {
            Integer.parseInt(a);
        } catch (NumberFormatException e) {
            throw new ArgumentError("Must input a integer or None!");
        }

        return a;
    }

    static CommandParser _instance = null;

    static CommandParser getCommandParser() {
        if(_instance == null) {
            _instance = new CommandParser();
        }

        return _instance;
    }
}
