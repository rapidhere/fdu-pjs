package rapid.ui;

import rapid.Env;
import rapid.ctrl.GameCommandEvent;
import rapid.widget.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class : ChoosePanel
 * Version : 0.1
 * Usage : The Choose Panel
 */

public class ChoosePanel extends GenericPanel {
    // the Background image
    private ImageIcon bgImg;

    // current user
    private User currentUser;

    // choose buttons
    private ChooseButton bts[][] = new ChooseButton[3][3];

    // constructor
    public ChoosePanel(User cu) {
        currentUser = cu;
        bgImg = new ImageIcon(Env.PIC_DIRECTORY + "Select.png");
        setSize(bgImg.getIconWidth(), bgImg.getIconHeight());

        final ChoosePanel self = this;
        // create buttons
        for(int i = 0;i < 9;i ++) {
            ChooseButton bt = new ChooseButton(i + 1, i >= currentUser.getHighLv());
            add(bt);
            bts[i % 3][i / 3] = bt;

            if(i < currentUser.getHighLv()) {
                final int j = i;
                bt.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            currentUser.getMap().loadLevel(j + 1);
                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }

                        gl.onBackToLevel(new GameCommandEvent(null, self));
                    }
                });
            }
        }
    }

    public void paintComponent(Graphics g) {
        // draw bg
        g.drawImage(bgImg.getImage(), 0, 0, this);

        // draw buttons
        for(int i = 0;i < 3;i ++) {
            for(int j = 0;j < 3;j ++) {
                bts[i][j].resize();
                bts[i][j].setLocation(80 + i * 100, 100 + j * 100);
            }
        }
    }
}

// special button of ChoosePanel
class ChooseButton extends JButton {
    private ImageIcon ico; // img
    private int lv; // level of button

    ChooseButton(int lv, boolean locked /*locked or not*/) {
        this.lv = lv;
        ico = getIcon(locked);
        setIcon(ico);
    }

    public void resize() {
        setSize(ico.getIconWidth(), ico.getIconHeight());
    }

    private ImageIcon getIcon(boolean locked) {
        if(locked) {
            return new ImageIcon(Env.PIC_DIRECTORY + String.format("l%df.png", lv));
        }

        return new ImageIcon(Env.PIC_DIRECTORY + String.format("l%dt.png", lv));
    }
}