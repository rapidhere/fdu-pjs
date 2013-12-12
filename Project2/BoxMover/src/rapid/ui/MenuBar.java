package rapid.ui;

import rapid.ctrl.GameCommandListener;

import javax.swing.*;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class MenuBar extends JMenuBar {
    public final Object MENU_ARCH[][][] = {
        {
            {"Game"},
            {"Restart Level",},
            {"Select Level",},
            null,
            {"Save",},
            null,
            {"Exit",}
        },
        {
            {"Help"},
            {"Instruction",},
            {"Author",}
        },
        {
            {"Rank"},
            {"Rank",}
        },
    };

    private GameCommandListener gl;

    public MenuBar() {
        for(int i = 0;i < MENU_ARCH.length;i ++) {
            add(buildMenu(MENU_ARCH[i]));
        }
    }

    private JMenu buildMenu(Object config[][]) {
        JMenu menu = new JMenu((String)config[0][0]);

        for(int i = 1;i < config.length;i ++) {
            if(config[i] == null) {
                menu.addSeparator();
            } else {
                menu.add(new JMenuItem((String)config[i][0]));
            }
        }

        return menu;
    }

    public void setGameListener(GameCommandListener gl) {
        this.gl = gl;
    }
}
