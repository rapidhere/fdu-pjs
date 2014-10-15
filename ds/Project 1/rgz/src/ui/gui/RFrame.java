package ui.gui;

import core.tar.Root;
import ui.Config;

import javax.swing.*;
import java.awt.*;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */

public class RFrame extends JFrame {
    private static RFrame theFrame = null;

    private Root root;
    private RMenuBar menuBar;
    private RMenuTableViewer tableViewer;
    private RMenuTreeViewer treeViewer;
    private RInfoViewer infoViewer;

    public static RFrame getFrame() {
        if(theFrame == null) {
            theFrame = new RFrame();
        }
        return theFrame;
    }

    private RFrame() {
        // set Title
        super("RapiD GZ Demo " + Config.version);

        // quit auto
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // set Size
        setSize(600, 450);

        // set Location to the middle of window
        setLocationRelativeTo(null);

        // add menu
        menuBar = new RMenuBar();
        setJMenuBar(menuBar);

        // set layout
        setUpLayout();

        // create root;
        root = new Root();
    }

    private void setUpLayout() {
        // set up layout manager
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // create tree viewer and table viewer
        treeViewer = new RMenuTreeViewer();
        tableViewer = new RMenuTableViewer();
        infoViewer = new RInfoViewer();

        // add to layout
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.2;
        c.weighty = 0.7;
        JPanel jp = new JPanel(new BorderLayout());
        jp.add(treeViewer, BorderLayout.CENTER);
        add(jp, c);

        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.8;
        c.weighty = 0.7;
        add(new JScrollPane(tableViewer), c);

        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1.0;
        c.weighty = 0.3;
        c.gridwidth = 2;
        jp = new JPanel(new BorderLayout());
        jp.add(infoViewer, BorderLayout.CENTER);
        jp.setBorder(BorderFactory.createLineBorder(Color.black));
        add(jp, c);

        // set visible
        treeViewer.setVisible(true);
        tableViewer.setVisible(true);
        infoViewer.setVisible(true);
    }

    public static void main(String args[]) {
        RFrame.getFrame().setVisible(true);
    }
}
