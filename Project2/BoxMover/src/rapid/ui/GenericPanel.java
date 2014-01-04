package rapid.ui;

import rapid.ctrl.GameCommandListener;

import javax.swing.*;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class : Generic Panel
 * Version : 0.1
 * Usage : Generic Game Panel
 */
abstract public class GenericPanel extends JPanel {
    protected GameCommandListener gl;

    public GenericPanel() {
    }

    public void setGameListener(GameCommandListener gl) {
        this.gl = gl;
    }

    // do something on exit
    public void destroy() {

    }
}
