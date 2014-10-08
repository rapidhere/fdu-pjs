package bin.exp;

import java.util.ArrayList;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */

public class MatchMoreThanOneCommand extends BMException {
    private String cmd_name;
    private ArrayList matched_cmds = new ArrayList();

    public MatchMoreThanOneCommand(String cmd_name) {
        this.cmd_name = cmd_name;
    }

    public int getMatchedSize() {
        return matched_cmds.size();
    }

    public String get(int index) {
        return (String)matched_cmds.get(index);
    }

    public void appendNewMatch(String cmd) {
        matched_cmds.add(cmd);
    }

    public String getCmdName() {
        return cmd_name;
    }
}
