package ui.gui;

import javax.swing.*;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class RInfoViewer extends JTextArea {
    public RInfoViewer() {
        super("RapiD's GZ Demo ver 0.1\nAll rights reserved(C): RapiD is a JOKER, rapidehere@gmail.com\n");
        setEditable(false);
    }

    @Override
    public void append(String msg) {
        super.append(msg);

        // make auto scroll
        setCaretPosition(getDocument().getLength());
    }
}
