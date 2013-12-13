package rapid.ui;

import rapid.Env;
import rapid.ctrl.CommandId;
import rapid.ctrl.GameCommandEvent;
import rapid.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
abstract public class InfoPanel extends GenericPanel {
    private ImageIcon bg;
    protected JButton bt;

    public InfoPanel(String bgPath, String btPath, boolean flagExit) {
        bg = new ImageIcon(bgPath);
        bt = utils.createButton(btPath);
        setSize(bg.getIconWidth(), bg.getIconHeight());
        add(bt);

        if(flagExit) {
            final InfoPanel self = this;

            bt.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    gl.onBackToLevel(new GameCommandEvent(CommandId.BACK_TO_LEVEL, null, self));
                }
            });
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(bg.getImage(), 0, 0, this);
        bt.setLocation((getWidth() - bt.getWidth()) / 2, 300);
    }
}
