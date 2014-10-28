package ui.gui;

import ui.UIEntry;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * The GUI Entry of the program
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
