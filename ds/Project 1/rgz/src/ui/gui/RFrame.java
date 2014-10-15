package ui.gui;

import core.tar.FileNode;
import core.tar.Root;
import excs.DCException;
import excs.TarException;
import ui.Config;

import javax.swing.*;
import java.awt.*;
import java.io.File;

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
    private boolean rootUpdated = false;
    private File srcFile = null;
    private RMenuBar menuBar;
    private RMenuTableViewer tableViewer;
    private RMenuTreeViewer treeViewer;
    private RInfoViewer infoViewer;
    private FileNode currentFn;

    public static RFrame getFrame() {
        if(theFrame == null) {
            theFrame = new RFrame();
        }
        return theFrame;
    }

    private RFrame() {
        // set Title
        super("RapiD's GZ Demo " + Config.version);

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

        // clear root;
        root = null;
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
        add(new JScrollPane(treeViewer), c);

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
        add(new JScrollPane(infoViewer), c);

        // set visible
        treeViewer.setVisible(true);
        tableViewer.setVisible(true);
        infoViewer.setVisible(true);
    }

    public static void main(String args[]) {
        RFrame.getFrame().setVisible(true);
    }

    private boolean checkRootUpdated() {
        if(! rootUpdated)
            return true;

        int ret = JOptionPane.showConfirmDialog(
            this, "Current rgz file haven't saved, are you sure to drop it?", "Warning", JOptionPane.YES_NO_OPTION);

        return ret == 0;
    }

    void putErrorInfo(String msg) {
        infoViewer.append("#Error: " + msg + "\n");
    }

    void putNormalInfo(String msg) {
        infoViewer.append(msg + "\n");
    }

    void setCurrentFileNode(FileNode fn) {
        currentFn = fn;
        putNormalInfo("Selected file/menu: " + fn.getPath());
        tableViewer.refreshWithFileNode(currentFn);
    }

    void openNewFile() {
        if(! checkRootUpdated()) {
            return ;
        }

        JFileChooser fc = new JFileChooser();
        int retVal = fc.showOpenDialog(this);

        if(retVal == JFileChooser.APPROVE_OPTION) {
            srcFile = fc.getSelectedFile();
            root = new Root();
            try {
                root.loadIndexFromFile(srcFile.getPath());
            } catch (TarException e) {
                putErrorInfo(e.getMessage());
                return ;
            }

            // update to viewer
            treeViewer.buildFromRoot(root);
        }
        rootUpdated = false;
    }

    void createNewFile() {
        if(! checkRootUpdated()) {
            return ;
        }

        root = new Root();
        treeViewer.buildFromRoot(root);
        setCurrentFileNode(root);

        rootUpdated = true;
    }

    void addSourceToRoot() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fc.setAcceptAllFileFilterUsed(false);

        int retVal = fc.showOpenDialog(this);

        if(retVal == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            try {
                root.addSource(f.getPath());
            } catch (TarException e) {
                putErrorInfo(e.getMessage());
                root = null;
            }
        }

        // update
        treeViewer.buildFromRoot(root);
        setCurrentFileNode(root);
    }

    void compress() {

    }

    void decompress(FileNode[] fns) {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);

        int retVal = fc.showOpenDialog(this);

        if(retVal == JFileChooser.APPROVE_OPTION) {
            File outputDir = fc.getSelectedFile();
            System.err.println(outputDir.getAbsolutePath());

            try {
                root.decompress(outputDir.getAbsolutePath(), fns, srcFile.getPath());
            } catch (TarException e) {
                putErrorInfo(e.getMessage());
                root = null;
            } catch (DCException e) {
                putErrorInfo(e.getMessage());
                root = null;
            }

        }
    }
}
