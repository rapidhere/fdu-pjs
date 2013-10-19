package bin.exp;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class CommandNotFound extends BMException {
    private String cmd_name;

    public CommandNotFound(String cmd_name) {
        this.cmd_name = cmd_name;
    }

    public String getCommandName() {
        return cmd_name;
    }
}
