package ui.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.security.Key;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class RMenuBar extends JMenuBar {
     public RMenuBar() {
        loadMenuBarConfig();
    }

    public void loadMenuBarConfig() {
        JMenuItem menuItem;

        // File Menu
        JMenu menu = new JMenu("File");
        menu.setMnemonic('F');
        add(menu);

        // File -> new
        menuItem = new JMenuItem("new");
        menu.add(menuItem);

        // File -> open
        menuItem = new JMenuItem("open");
        menu.add(menuItem);

        // sep
        menu.addSeparator();

        // File -> compress to
        menuItem = new JMenuItem("compress");
        menu.add(menuItem);

        // File -> decompress to
        menuItem = new JMenuItem("decompress");
        menu.add(menuItem);

        // File -> add source
        menuItem = new JMenuItem("add source");
        menu.add(menuItem);

        // sep
        menu.addSeparator();

        // File -> quit
        menuItem = new JMenuItem("quit");
        menu.add(menuItem);

        // Help Menu
        menu = new JMenu("Help");
        menu.setMnemonic('H');
        add(menu);

        // Help->Help
        menuItem = new JMenuItem("help");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        menu.add(menuItem);

        // Separator
        menu.addSeparator();
        // Help->About
        menuItem = new JMenuItem("about");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, KeyEvent.CTRL_DOWN_MASK));
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        menu.add(menuItem);
    }
}
