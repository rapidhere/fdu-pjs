package ui.gui;

import core.dc.*;

import javax.swing.*;
import javax.swing.plaf.metal.MetalIconFactory;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

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

    JMenuItem compressMenuItem, addSourceMenuItem;
    JMenu dcmMenu, dcMenu, caMenu, configMenu;

    public void loadMenuBarConfig() {
        JMenuItem menuItem;

        // File Menu
        JMenu menu = new JMenu("File");
        menu.setMnemonic('F');
        add(menu);

        // File -> new
        menuItem = new JMenuItem("new", new MetalIconFactory.TreeLeafIcon());
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        menuItem.addActionListener(e9 -> RFrame.getFrame().createNewFile());
        menu.add(menuItem);

        // File -> open
        menuItem = new JMenuItem("open", new MetalIconFactory.TreeFolderIcon());
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        menuItem.addActionListener(e8 -> RFrame.getFrame().openNewFile());
        menu.add(menuItem);

        // sep
        menu.addSeparator();

        // File -> compress to
        menuItem = new JMenuItem("compress");
        compressMenuItem = menuItem;
        menuItem.addActionListener(e7 -> RFrame.getFrame().compress());
        menu.add(menuItem);

        // File -> add source
        menuItem = new JMenuItem("add source");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        menuItem.addActionListener(e6 -> RFrame.getFrame().addSourceToRoot());
        addSourceMenuItem = menuItem;
        menu.add(menuItem);

        // sep
        menu.addSeparator();

        // File -> quit
        menuItem = new JMenuItem("quit");
        menuItem.addActionListener(e5 -> RFrame.getFrame().exit());
        menu.add(menuItem);

        // Config
        menu = new JMenu("Config");
        menu.setMnemonic('C');
        configMenu = menu;
        add(menu);

        // Config -> dcm
        menuItem = new JMenu("dcm");
        dcmMenu = (JMenu)menuItem;
        menu.add(menuItem);

        // Config -> dcm -> Block DCMs
        String[] dcmLabels = {
            "64k block dcm",
            "128k block dcm",
            "256k block dcm",
            "512k block dcm",
            "1m block dcm",
            "4m block dcm",
            "16m block dcm",
        };
        createMenuButtonGroup(dcmMenu, dcmLabels, (e4, index) -> {
            RFrame fr = RFrame.getFrame();
            switch (index) {
                case 0: fr.setDcm(new BlockDCM(null, null, 64 * 1024)); break;
                case 1: fr.setDcm(new BlockDCM(null, null, 128 * 1024)); break;
                case 2: fr.setDcm(new BlockDCM(null, null, 256 * 1024)); break;
                case 3: fr.setDcm(new BlockDCM(null, null, 512 * 1024)); break;
                case 4: fr.setDcm(new BlockDCM(null, null, 1024 * 1024)); break;
                case 5: fr.setDcm(new BlockDCM(null, null, 4 * 1024 * 1024)); break;
                case 6: fr.setDcm(new BlockDCM(null, null, 16 * 1024 * 1024)); break;
            }
        });

        // Config -> dc algorithm
        menuItem = new JMenu("dc algorithm");
        dcMenu = (JMenu)menuItem;
        menu.add(menuItem);

        // Config -> dc algorithm -> algorithm
        String[] dcLabels = {
            "huffman tree",
        };
        createMenuButtonGroup(dcMenu, dcLabels, (e3, index) -> {
            switch (index) {
                case 0: RFrame.getFrame().setDc(new DCHuffmanAlgorithm());
            }
        });

        // Config -> ca algorithm
        menuItem = new JMenu("ca algorithm");
        caMenu = (JMenu)menuItem;
        menu.add(menuItem);

        // Config -> ca algorithm -> algorithms
        String[] caLabels = {
            "ascii",
            "1-bit",
            "2-bit",
            "4-bit",
            "1-byte",
            "2-byte",
            "3-byte",
            "4-byte"
        };
        createMenuButtonGroup(caMenu, caLabels, (e2, index) -> {
            RFrame fr = RFrame.getFrame();
            switch (index) {
                case 0: fr.setCa(new CatchASCIIAlgorithm()); break;
                case 1: fr.setCa(new CatchFixedBitsAlgorithm((byte)1)); break;
                case 2: fr.setCa(new CatchFixedBitsAlgorithm((byte)2)); break;
                case 3: fr.setCa(new CatchFixedBitsAlgorithm((byte)4)); break;
                case 4: fr.setCa(new CatchFixedBytesAlgorithm((byte)1)); break;
                case 5: fr.setCa(new CatchFixedBytesAlgorithm((byte)2)); break;
                case 6: fr.setCa(new CatchFixedBytesAlgorithm((byte)3)); break;
                case 7: fr.setCa(new CatchFixedBytesAlgorithm((byte)4)); break;
            }
        });

        // Help Menu
        menu = new JMenu("Help");
        menu.setMnemonic('H');
        add(menu);

        // Help->Help
        menuItem = new JMenuItem("help");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        menuItem.addActionListener(e1 -> JOptionPane.showMessageDialog(RFrame.getFrame(),
            "Ah.... this demo is very easy and got no help message :p",
            "Help",
            JOptionPane.INFORMATION_MESSAGE));
        menu.add(menuItem);

        // Separator
        menu.addSeparator();

        // Help->About
        menuItem = new JMenuItem("about");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, KeyEvent.CTRL_DOWN_MASK));
        menuItem.addActionListener(e -> JOptionPane.showMessageDialog(RFrame.getFrame(),
            "RapiD's GZ Demo ver 0.1, for DataStructure pj.\n\n(C) RapiD is a JOKER, rapidhere@gmail.com",
            "About",
            JOptionPane.INFORMATION_MESSAGE));
        menu.add(menuItem);
    }

    private void createMenuButtonGroup(JMenu m, String[] labels, menuButtonGroupCB _cb) {
        final menuButtonGroupCB cb = _cb;
        ButtonGroup bg = new ButtonGroup();

        for(int i = 0;i < labels.length;i ++) {
            JRadioButtonMenuItem rb = new JRadioButtonMenuItem(labels[i]);
            if(i == 0)
                rb.setSelected(true);
            else
                rb.setSelected(false);

            final int ii = i;
            rb.addActionListener(e -> cb.action(e, ii));

            m.add(rb);
            bg.add(rb);
        }
    }
}

interface menuButtonGroupCB {
    void action(ActionEvent e, int index);
}