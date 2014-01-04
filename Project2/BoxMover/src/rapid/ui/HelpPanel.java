package rapid.ui;

import rapid.Env;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class : HelpPanel
 * Version : 0.1
 * Usage : The Instruction Panel
 */
public class HelpPanel extends InfoPanel {
    public HelpPanel() {
        super(Env.PIC_DIRECTORY + "Help.jpg", Env.PIC_DIRECTORY + "back.png", true);
    }
}
