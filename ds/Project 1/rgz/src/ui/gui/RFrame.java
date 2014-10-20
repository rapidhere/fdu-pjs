package ui.gui;

import core.dc.*;
import core.tar.FileNode;
import core.tar.Root;
import excs.DCException;
import excs.TarException;
import ui.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
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
    private DCAlgorithm dc = new DCHuffmanAlgorithm();
    private CatchAlgorithm ca = new CatchASCIIAlgorithm();
    private DCM dcm = new BlockDCM(dc, ca, 64 * 1024);

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

        // sync menu
        updateMenuState();

        // set visible
        treeViewer.setVisible(true);
        tableViewer.setVisible(true);
        infoViewer.setVisible(true);
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
        FileNode currentFn = fn;
        if(! (fn instanceof  Root))
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
        updateMenuState();
    }

    void createNewFile() {
        if(! checkRootUpdated()) {
            return ;
        }

        root = new Root();
        treeViewer.buildFromRoot(root);
        setCurrentFileNode(root);

        rootUpdated = true;
        updateMenuState();
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
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fc.setAcceptAllFileFilterUsed(false);

        int retVal = fc.showOpenDialog(this);

        if(retVal == JFileChooser.APPROVE_OPTION) {
            putNormalInfo("Starting compress ...");
            File f = fc.getSelectedFile();
            if(f.exists()) {
                int choice = JOptionPane.showConfirmDialog(
                    this,
                    "File " + f.getPath() + "exists, are you sure to replace it?",
                    "Warning",
                    JOptionPane.YES_NO_OPTION);

                if(choice != 0) { // canceled
                    return ;
                }
            }

            try {
                dcm.setCA(ca);
                dcm.setDC(dc);
                root.compress(f.getAbsolutePath(), dcm);
            } catch (TarException e) {
                putErrorInfo(e.getMessage());
                return;
            } catch (DCException e) {
                putErrorInfo(e.getMessage());
                return;
            }

            putNormalInfo("Compress done, updating info ...");

            rootUpdated = false;
            updateMenuState();
            root.remarkSize();
            treeViewer.buildFromRoot(root);
            putNormalInfo("info updated.");
        }
    }

    void updateMenuState() {
        if(rootUpdated) {
            menuBar.addSourceMenuItem.setEnabled(true);
            menuBar.compressMenuItem.setEnabled(true);
            menuBar.configMenu.setEnabled(true);
            tableViewer.decompresMenuItem.setEnabled(false);
        } else {
            menuBar.addSourceMenuItem.setEnabled(false);
            menuBar.compressMenuItem.setEnabled(false);
            menuBar.configMenu.setEnabled(false);
            tableViewer.decompresMenuItem.setEnabled(true);
        }
    }

    void decompress(FileNode[] fns) {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);

        int retVal = fc.showOpenDialog(this);

        if(retVal == JFileChooser.APPROVE_OPTION) {
            putNormalInfo("Starting decompress ...");
            File outputDir = fc.getSelectedFile();

            try {
                root.decompress(outputDir.getAbsolutePath(), fns, srcFile.getPath());
            } catch (TarException e) {
                putErrorInfo(e.getMessage());
                root = null;
            } catch (DCException e) {
                putErrorInfo(e.getMessage());
                root = null;
            }
            putNormalInfo("Decompress done.");
        }
    }

    void setDcm(DCM dcm) {
        this.dcm = dcm;
    }

    void setDc(DCAlgorithm dc) {
        this.dc = dc;
    }

    void setCa(CatchAlgorithm ca) {
        this.ca = ca;
    }

    void exit() {
        if(! checkRootUpdated()) {
            return ;
        }
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}
