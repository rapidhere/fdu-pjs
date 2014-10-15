package ui.gui;

import javax.swing.*;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class RMenuBar extends JMenuBar {
     public RMenuBar() {
        loadMenuBarConfig();
    }

    public void loadMenuBarConfig() {
        add(new JMenu("test"));
    }
}
