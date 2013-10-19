package bin.ui.cmd;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class Command {
    Object arg;
    String cmd_name;

    public Command(String cmd_name) {
        this.cmd_name = cmd_name;
        arg = null;
    }

    public Command(String cmd_name,int arg) {
        this.cmd_name = cmd_name;
        this.arg = arg;
    }

    public String getCommandName() {
        return cmd_name;
    }

    public int getArgument() {
        if(arg == null) {
            return 0;
        }
        return (Integer)arg;
    }

    public boolean hasArgument() {
        return arg != null;
    }
}
