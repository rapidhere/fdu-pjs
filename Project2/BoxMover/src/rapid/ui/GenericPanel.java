package rapid.ui;

import rapid.ctrl.GameCommandEvent;
import rapid.ctrl.GameCommandListener;

import javax.swing.*;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
abstract public class GenericPanel extends JPanel {
    protected GameCommandListener gl;

    public GenericPanel() {
    }

    public void setGameListener(GameCommandListener gl) {
        this.gl = gl;
    }
}
