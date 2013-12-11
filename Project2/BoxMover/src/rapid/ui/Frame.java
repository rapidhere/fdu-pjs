package rapid.ui;

import rapid.Env;
import rapid.ctrl.GameCommandListener;

import javax.swing.*;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class Frame extends JFrame {
    private GenericPanel curPanel;
    private GameCommandListener gListener;


    public Frame() {
        super();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(Env.FRAME_TITLE);
        setResizable(false);

        setVisible(true);
    }

    public void setGameState(GenericPanel p, GameCommandListener g, JMenuBar jmb) {
        curPanel = p;
        curPanel.setGameListener(g);

        setContentPane(curPanel);
        setSize(curPanel.getSize());

        if(jmb != null) {
            setJMenuBar(jmb);
        }

        curPanel.requestFocus();
    }
}
