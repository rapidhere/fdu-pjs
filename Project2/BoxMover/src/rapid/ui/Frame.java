package rapid.ui;

import rapid.Env;
import rapid.ctrl.CommandId;
import rapid.ctrl.GameCommandEvent;
import rapid.ctrl.GameCommandListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class Frame extends JFrame {
    private GenericPanel curPanel;
    private GameCommandListener gl;

    public Frame(GameCommandListener _gl) {
        super();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(Env.FRAME_TITLE);
        setResizable(false);

        setVisible(true);

        this.gl = _gl;

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                gl.onExit(new GameCommandEvent(CommandId.EXIT, null, this));
            }
        });
    }

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
