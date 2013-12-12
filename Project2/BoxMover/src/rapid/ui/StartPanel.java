package rapid.ui;

import rapid.Env;
import rapid.ctrl.CommandId;
import rapid.ctrl.GameCommandEvent;
import rapid.ctrl.GameCommandListener;

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
public class StartPanel extends GenericPanel {
    private ImageIcon bgImg;

    private JButton buttons[] = new JButton[3];
    private static final int
        BUTTON_START = 0,
        BUTTON_CONTINUE = 1,
        BUTTON_EXIT = 2;

    private final Object BUTTON_CONFIG[][] = {
        {Env.PIC_DIRECTORY + "start.png", 200},
        {Env.PIC_DIRECTORY + "continue.png", 240},
        {Env.PIC_DIRECTORY + "ex.png", 280},
    };

    public StartPanel() {
        super();

        // load Background Image
        bgImg = new ImageIcon(Env.PIC_DIRECTORY + "Cover.png");
        setSize(bgImg.getIconWidth(), bgImg.getIconHeight());

        // create Buttons
        for(int i = 0; i < buttons.length;i ++) {
            buttons[i] = createButton((String)BUTTON_CONFIG[i][0]);
            add(buttons[i]);
        }
    }

    public void paintComponent(Graphics g) {
        g.drawImage(bgImg.getImage(), 0, 0, this);

        for(int i = 0;i < buttons.length;i ++) {
            JButton bt = buttons[i];
            bt.setLocation((getWidth() - bt.getWidth()) / 2, (Integer)BUTTON_CONFIG[i][1]);
        }
    }

    private JButton createButton(String path) {
        JButton bt = new JButton(new ImageIcon(path));
        bt.setMargin(new Insets(0, 0, 0, 0));
        bt.setBorder(BorderFactory.createEtchedBorder());
        bt.setContentAreaFilled(false);

        return bt;
    }

    public void setGameListener(GameCommandListener g) {
        super.setGameListener(g);
        final StartPanel self = this;

        buttons[BUTTON_EXIT].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gl.onExit(new GameCommandEvent(CommandId.EXIT, null, self));
            }
        });

        buttons[BUTTON_CONTINUE].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gl.onOldUser(new GameCommandEvent(CommandId.OLD_USER, null, self));
            }
        });

        buttons[BUTTON_START].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gl.onNewUser(new GameCommandEvent(CommandId.NEW_USER, null, self));
            }
        });
    }
}
