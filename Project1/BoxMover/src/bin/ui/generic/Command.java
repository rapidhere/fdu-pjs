package bin.ui.generic;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class : Command
 * Version : ver 0.1
 * Usage : The Generic Command
 */
public class Command {
    private Object arg;
    private int cmd_id;

    public Command(int cmd_id) {
        this.cmd_id = cmd_id;
        arg = null;
    }

    public Command(int cmd_id,int arg) {
        this.cmd_id = cmd_id;
        this.arg = arg;
    }

    public int getCommandId() {
        return cmd_id;
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
