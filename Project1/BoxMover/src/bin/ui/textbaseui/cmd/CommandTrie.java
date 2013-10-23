package bin.ui.textbaseui.cmd;

import bin.exp.CommandNotFound;
import bin.exp.MatchMoreThanOneCommand;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
class CommandTrieNode {
    public int cmd_id;
    public CommandTrieNode child[] = new CommandTrieNode[26];

    CommandTrieNode() {
        cmd_id = -1;
        for(int i = 0;i < child.length;i ++) {
            child[i] = null;
        }
    }

    int getChildrenCount() {
        int ret = 0;
        for(int i = 0;i < child.length;i ++) {
            if(child[i] != null) {
                ret ++;
            }
        }

        return ret;
    }
}

public class CommandTrie {
    private CommandTrieNode root;
    private int cmd_types[] = new int[1000];
    private int ncmd = 0;
    private String cmd_full_name = "";
    private int cmd_type = -1;

    public CommandTrie() {
        root = new CommandTrieNode();
    }

    public void registerCommand(String cmd_name,int arg_type) {
        CommandTrieNode ptr = root;

        for(int i = 0;i < cmd_name.length();i ++) {
            int ind = cmd_name.charAt(i) - 'a';

            if(ptr.child[ind] == null) {
                ptr.child[ind] = new CommandTrieNode();
            }
            ptr = ptr.child[ind];
        }

        ptr.cmd_id = ncmd;
        cmd_types[ncmd ++] = arg_type;
    }

    private static int buildNode(CommandTrieNode cur) {
        int n_child = cur.getChildrenCount();
        if(n_child == 0) {
            return cur.cmd_id;
        }

        if(n_child == 1) {
            for(int i = 0;i < cur.child.length;i ++) {
                if(cur.child[i] != null) {
                    int k = buildNode(cur.child[i]);
                    if(k != -1) {
                        cur.cmd_id = k;
                    }
                    return k;
                }
            }
        }

        for(int i = 0;i < cur.child.length;i ++) {
            if(cur.child[i] != null) {
                buildNode(cur.child[i]);
            }
        }

        return -1;
    }

    public void buildTrie() {
        buildNode(root);
    }

    public void parseCommandName(String cmd_name)
    throws CommandNotFound, MatchMoreThanOneCommand {
        CommandTrieNode ptr = root;
        int i;
        for(i = 0;i < cmd_name.length();i ++) {
            int ind = cmd_name.charAt(i) - 'a';
            if(ind < 0 || ind >= 26) {
                throw new CommandNotFound(cmd_name);
            }

            if(ptr.child[ind] == null) {
                break;
            }

            ptr = ptr.child[ind];
        }

        if(i != cmd_name.length()) {
            throw new CommandNotFound(cmd_name);
        }

        if(ptr.cmd_id == -1) {
            MatchMoreThanOneCommand e = new MatchMoreThanOneCommand(cmd_name);
            appendMatchCommands(e, ptr, cmd_name);
            throw e;
        }

        cmd_full_name = cmd_name;
        while(true) {
            if(ptr.getChildrenCount() == 0) {
                break;
            }

            for(i = 0;i < ptr.child.length;i ++) {
                if(ptr.child[i] != null) {
                    cmd_full_name += (char)('a' + i);
                    ptr = ptr.child[i];
                }
            }
        }
        cmd_type = cmd_types[ptr.cmd_id];
    }

    public String getCommandFullName() {
        return cmd_full_name;
    }

    public int getCommandArgumentType() {
        return cmd_type;
    }

    static private void appendMatchCommands(MatchMoreThanOneCommand e, CommandTrieNode ptr, String cur) {
        if(ptr.getChildrenCount() == 0) {
            e.appendNewMatch(cur);
            return;
        }

        for(int i = 0;i < ptr.child.length;i ++) {
            if(ptr.child[i] != null) {
                appendMatchCommands(e, ptr.child[i], cur + (char)('a' + i));
            }
        }
    }
}
