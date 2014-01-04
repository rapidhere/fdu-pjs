package rapid.ui;
import rapid.ctrl.GameCommandEvent;
import rapid.ctrl.GameCommandListener;

import javax.swing.*;
import java.awt.event.*;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class : MenuBar
 * Version : 0.1
 * Usage : The Menu bar of game
 */
public class MenuBar extends JMenuBar {
    private MenuBar self = this;
    // The structure of menu bar
    public final Object MENU_ARCH[][][] = {
        {
            {"Game"},
            {"Restart Level", new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    gl.onRestartLevel(new GameCommandEvent(null, self));
                }
            }},
            {"Select Level", new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    gl.onChooseLevel(new GameCommandEvent(null, self));
                }
            }},
            null,
            {"Save", new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    gl.onSaveLevel(new GameCommandEvent(null, self));
                }
            }},
            null,
            {"Exit", new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    gl.onExit(new GameCommandEvent(null, self));
                }
            }}
        },
        {
            {"Help"},
            {"Instruction", new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    gl.onShowHelp(new GameCommandEvent(null, self));
                }
            }},
            {"Author", new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    gl.onShowAbout(new GameCommandEvent(null, self));
                }
            }}
        },
        {
            {"Rank"},
            {"Rank", new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    gl.onShowRank(new GameCommandEvent(null, self));
                }
            }}
        },
    };

    private GameCommandListener gl;

    public MenuBar(GameCommandListener gl) {
        this.gl = gl;
        for(int i = 0;i < MENU_ARCH.length;i ++) {
            add(buildMenu(MENU_ARCH[i]));
        }
    }

    // build menu
    private JMenu buildMenu(Object config[][]) {
        JMenu menu = new JMenu((String)config[0][0]);

        for(int i = 1;i < config.length;i ++) {
            if(config[i] == null) {
                menu.addSeparator();
            } else {
                JMenuItem t = new JMenuItem((String)config[i][0]);
                t.addActionListener((ActionListener)config[i][1]);
                menu.add(t);
            }
        }

        return menu;
    }
}
