package ui.gui;

import ui.UIEntry;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class GUIEntry extends UIEntry {
    @Override
    protected void run() {
        RFrame.getFrame().setVisible(true);
    }

    public static void main(String args[]) {
        UIEntry entry = new GUIEntry();
        entry.start();
    }
}
