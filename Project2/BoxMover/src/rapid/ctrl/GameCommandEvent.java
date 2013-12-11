package rapid.ctrl;

import java.util.EventObject;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class GameCommandEvent extends EventObject {
    public CommandId cmdId;
    public Object cmdArgs[];

    public GameCommandEvent(CommandId cmdId, Object cmdArgs[], Object source) {
        super(source);
        this.cmdId = cmdId;
        this.cmdArgs = cmdArgs;
    }
}
