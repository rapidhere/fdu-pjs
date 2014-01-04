package rapid.ui;

import rapid.ctrl.GameCommandEvent;
import rapid.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class : InfoPanel
 * Version : 0.1
 * Usage : A Abstract Panel to show information
 */
abstract public class InfoPanel extends GenericPanel {
    private ImageIcon bg;   // bg of info panel
    protected JButton bt;   // the back button

    public InfoPanel(String bgPath, String btPath, boolean flagExit) {
        bg = new ImageIcon(bgPath);
        bt = utils.createButton(btPath);
        setSize(bg.getIconWidth(), bg.getIconHeight());
        add(bt);

        if(flagExit) {  // use default exit button
            final InfoPanel self = this;

            bt.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    gl.onBackToLevel(new GameCommandEvent(null, self));
                }
            });
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // draw bg
        g.drawImage(bg.getImage(), 0, 0, this);

        // draw button
        bt.setLocation((getWidth() - bt.getWidth()) / 2, 320);
    }
}
