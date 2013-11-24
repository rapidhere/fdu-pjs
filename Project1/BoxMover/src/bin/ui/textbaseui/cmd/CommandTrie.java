package bin.ui.textbaseui.cmd;

import bin.exp.CommandNotFound;
import bin.exp.MatchMoreThanOneCommand;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :      CommandTrie
 * Version :    ver 0.1
 * Usage :      The Trie Structure store the commands
 *              For more info please refer to developer manual
 */

/**
 * The Node in the trie
 */
class CommandTrieNode {
    public int cmd_id;      // the identifier allocate by Trie
    public CommandTrieNode child[] = new CommandTrieNode[27];   // children

    /**
     * Constructor, clear all
     */
    CommandTrieNode() {
        cmd_id = -1;
        for(int i = 0;i < child.length;i ++) {
            child[i] = null;
        }
    }

    /**
     * Get the amount of children
     * @return
     */
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
    private CommandTrieNode root;               // the root node of trie
    private int cmd_types[] = new int[1000];    // the cmd_type of Command
    private int ncmd = 0;                       // the amount of cmds in trie
    private String cmd_full_name = "";          // the full name of last parsed command
    private int cmd_type = -1;                  // the cmd_type of last parsed command

    /**
     * Constructor
     */
    public CommandTrie() {
        root = new CommandTrieNode();
    }

    /**
     * Register a Command into trie
     * @param cmd_name the full name of command
     * @param arg_type the argument type of command
     */
    public void registerCommand(String cmd_name,int arg_type) {
        CommandTrieNode ptr = root;

        // Loop in trie
        for(int i = 0;i < cmd_name.length();i ++) {
            int ind = cmd_name.charAt(i) - 'a';

            if(ptr.child[ind] == null) { // no such child, allocate new one
                ptr.child[ind] = new CommandTrieNode();
            }
            ptr = ptr.child[ind];
        }

        // store it
        ptr.cmd_id = ncmd;
        cmd_types[ncmd ++] = arg_type;
    }

    /**
     * Build from a node
     * @param cur the Node to build
     * @return the cmd_id
     */
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

    /**
     * After register all of the Commands, Trie need to build the information
     */
    public void buildTrie() {
        buildNode(root);
    }

    /**
     * Parse a command, gets it's full_name and cmd_type
     * @param cmd_name the Command Name to parse
     * @throws CommandNotFound
     * @throws MatchMoreThanOneCommand
     */
    public void parseCommandName(String cmd_name)
    throws CommandNotFound, MatchMoreThanOneCommand {
        CommandTrieNode ptr = root;
        int i;
        // Loop in trie
        for(i = 0;i < cmd_name.length();i ++) {
            int ind = cmd_name.charAt(i) - 'a';
            if(ind < 0 || ind >= 26) { // Illegal character, Illegal Command Name
                throw new CommandNotFound(cmd_name);
            }

            if(ptr.child[ind] == null) {    // End of trie break
                break;
            }

            ptr = ptr.child[ind];
        }

        // No such Command in the trie
        if(i != cmd_name.length()) {
            throw new CommandNotFound(cmd_name);
        }

        // This TrieNode is belong to more than on Command
        if(ptr.cmd_id == -1) {
            MatchMoreThanOneCommand e = new MatchMoreThanOneCommand(cmd_name);
            appendMatchCommands(e, ptr, cmd_name);
            throw e;
        }

        // Build Full Name
        cmd_full_name = cmd_name;   // prefix
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
        // Get CmdType
        cmd_type = cmd_types[ptr.cmd_id];
    }

    /**
     * Get the full name of last Command
     * @return
     */
    public String getCommandFullName() {
        return cmd_full_name;
    }

    /**
     * Get the type of last Command
     * @return
     */
    public int getCommandArgumentType() {
        return cmd_type;
    }

    // Used to Build Exception MatchMoreThanOneCommand
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
