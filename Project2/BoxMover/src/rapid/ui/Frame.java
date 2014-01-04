package rapid.ui;

import rapid.Env;
import rapid.ctrl.GameCommandEvent;
import rapid.ctrl.GameCommandListener;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class : Frame
 * Version : 0.1
 * Usage : The Frame of the game
 */
public class Frame extends JFrame {
    private GenericPanel curPanel;  // curPanel of Frame
    private GameCommandListener gl; // Game listener

    // constructor
    public Frame(GameCommandListener _gl) {
        super();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(Env.FRAME_TITLE);
        setResizable(false);

        setVisible(true);

        this.gl = _gl;

        // Handle exit event
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                gl.onExit(new GameCommandEvent(null, this));
            }
        });
    }

    // set the current state of game
    public void setGameState(GenericPanel p, GameCommandListener g, JMenuBar jmb) {
        if(curPanel != null) {
            curPanel.destroy();
            remove(curPanel);
        }

        curPanel = p;
        curPanel.setGameListener(g);
        setContentPane(curPanel);
        setJMenuBar(jmb);
        setSize(curPanel.getSize());


        curPanel.requestFocus();
    }
}
