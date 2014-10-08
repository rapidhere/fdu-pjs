package rapid.ctrl;

import java.util.EventObject;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class : GameCommandEvent
 * Version : 0.1
 * Usage : The event object of game
 */
public class GameCommandEvent extends EventObject {
    public Object cmdArgs[];

    public GameCommandEvent(Object cmdArgs[], Object source) {
        super(source);
        this.cmdArgs = cmdArgs;
    }
}
