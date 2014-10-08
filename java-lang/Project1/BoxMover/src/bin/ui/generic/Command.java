package bin.ui.generic;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class : Command
 * Version : ver 0.1
 * Usage :  The Generic Command raised by UserInterface
 *          For more info please refer to developer manual
 */

public class Command {
    private Object arg;     // The argument of Command, usually a integer
    private int cmd_id;     // the cmd_id of Command, see Env.CMD_*

    /**
     * Constructor with no argument
     * @param cmd_id the cmd_id of Command, see Env.CMD_*
     */
    public Command(int cmd_id) {
        this.cmd_id = cmd_id;
        arg = null;
    }

    /**
     * Constructor with a int argument
     * @param cmd_id the cmd_id of Command, see Env.CMD_*
     * @param arg  the argument
     */
    public Command(int cmd_id,int arg) {
        this.cmd_id = cmd_id;
        this.arg = arg;
    }

    /**
     * Get the Command ID
     * @return
     */
    public int getCommandId() {
        return cmd_id;
    }

    /**
     * get The Argument, if no argument will return 0
     * @return
     */
    public int getArgument() {
        if(arg == null) {
            return 0;
        }
        return (Integer)arg;
    }

    /**
     * Check if this command has argument
     * @return
     */
    public boolean hasArgument() {
        return arg != null;
    }
}
