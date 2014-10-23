package ui.gui;

import core.notify.*;
import core.tar.FileNode;
import core.tar.Root;
import excs.*;
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
    private Config conf;
    private JProgressBar mainProgressBar, subProgressBar;
    Thread runningThread = null;

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
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // set Size
        setSize(600, 500);

        // set Location to the middle of window
        setLocationRelativeTo(null);

        // add menu
        menuBar = new RMenuBar();
        setJMenuBar(menuBar);

        // set layout
        setUpLayout();

        // clear root;
        root = null;

        // default conf
        try {
            conf = new Config();
            conf.setFcId(Config.FACTORY_ASCII);
            conf.setDcId(Config.DC_HUFFMAN);
            conf.setDcmId(Config.DCM_BLOCK);
            conf.getFactory().getBlockDCM().setBlockSize(64 * 1024);
        } catch (UnknownFactoryId | UnknownDCMId | UnknownDCAlgorithmId e) {
            // never reach here
            e.printStackTrace();
        }

        // load notifier
        loadNotifier();
    }

    private void loadNotifier() {
        Notifier notifier = Notifier.getNotifier();

        notifier.register(MSGDCMCompressNew.class, msg -> {
            putNormalInfo("  Compressing: " + msg.getPath());
            mainProgressBar.setStringPainted(true);
            mainProgressBar.setMaximum(msg.getTotFile());
            mainProgressBar.setValue(msg.getNFile());
        });

        notifier.register(MSGBlockDCMStartNew.class, msg -> {
            subProgressBar.setStringPainted(true);
            subProgressBar.setString(String.format("Processing Block %d/%d",
                msg.getNumberBlock(), msg.getTotalBlock()));
            subProgressBar.setMaximum(msg.getTotalBlock());
            subProgressBar.setValue(msg.getNumberBlock());
        });

        notifier.register(MSGLoadingIndex.class, msg -> {
            putNormalInfo("  Loading index");
            mainProgressBar.setString("Loading Index ...");
            mainProgressBar.setIndeterminate(true);
        });

        notifier.register(MSGLoadedIndex.class, msg -> {
            putNormalInfo("  Loaded index");
            mainProgressBar.setString("");
            mainProgressBar.setIndeterminate(false);
        });

        notifier.register(MSGDumpingIndex.class, msg -> {
            putNormalInfo("  Dumping index");
            mainProgressBar.setString("  Dumping index");
            mainProgressBar.setIndeterminate(true);
        });

        notifier.register(MSGDumpedIndex.class, msg -> {
            putNormalInfo("  Dumped Index, update info");
            mainProgressBar.setString("");
            subProgressBar.setString("");
            mainProgressBar.setIndeterminate(false);
            rootUpdated = false;
            updateMenuState();
            root.remarkSize();
            treeViewer.buildFromRoot(root);
            try {
                runningThread.join();
            } catch (InterruptedException e) {
                putErrorInfo("Join thread failed: " + e.getMessage());
            }
        });

        notifier.register(MSGAddingSource.class, msg -> mainProgressBar.setIndeterminate(true));
        notifier.register(MSGTarBuildingIndex.class, msg ->
            putNormalInfo("  Found source file: " + msg.getPath()));
        notifier.register(MSGAddedSource.class, msg -> {
            mainProgressBar.setIndeterminate(false);
            try {
                runningThread.join();
            } catch (InterruptedException e) {
                putErrorInfo("Join thread failed: " + e.getMessage());
            }
        });

        notifier.register(MSGDCMDecompressNew.class, msg -> {
            putNormalInfo("  Decompressing: " + msg.getPath());
            mainProgressBar.setMaximum(msg.getTotFile());
            mainProgressBar.setValue(msg.getNFile());
        });

        notifier.register(MSGDCMDecompressNewFile.class, msg -> {
            subProgressBar.setIndeterminate(true);
            subProgressBar.setString(msg.getPath());
            subProgressBar.setStringPainted(true);
            putNormalInfo("    +> " + msg.getPath());
        });

        notifier.register(MSGDecompressDone.class, msg -> {
            putNormalInfo("Decompress Done");
            subProgressBar.setStringPainted(false);
            subProgressBar.setIndeterminate(false);
            try {
                runningThread.join();
            } catch (InterruptedException e) {
                putErrorInfo("Join thread failed: " + e.getMessage());
            }
        });
    }

    private void disableFrontEnd() {
        treeViewer.setEnabled(false);
        menuBar.configMenu.setEnabled(false);
        menuBar.fileMenu.setEnabled(false);
        menuBar.helpMenu.setEnabled(false);
        tableViewer.setEnabled(false);
    }

    private void enableFrontEnd() {
        treeViewer.setEnabled(true);
        menuBar.configMenu.setEnabled(true);
        menuBar.fileMenu.setEnabled(true);
        menuBar.helpMenu.setEnabled(true);
        tableViewer.setEnabled(true);

    }

    private void setUpLayout() {
        // set up layout manager
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // create tree viewer and table viewer
        treeViewer = new RMenuTreeViewer();
        tableViewer = new RMenuTableViewer();
        infoViewer = new RInfoViewer();
        mainProgressBar = new JProgressBar(0, 100);
        subProgressBar = new JProgressBar(0, 100);

        // add to layout
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.2;
        c.weighty = 0.6;
        add(new JScrollPane(treeViewer), c);

        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.8;
        c.weighty = 0.6;
        add(new JScrollPane(tableViewer), c);

        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1.0;
        c.weighty = 0.34;
        c.gridwidth = 2;
        add(new JScrollPane(infoViewer), c);

        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1.0;
        c.weighty = 0.03;
        c.gridwidth = 2;
        add(mainProgressBar, c);

        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 1.0;
        c.weighty = 0.03;
        c.gridwidth = 2;
        add(subProgressBar, c);

        // sync menu
        updateMenuState();

        // set visible
        treeViewer.setVisible(true);
        tableViewer.setVisible(true);
        infoViewer.setVisible(true);
        mainProgressBar.setVisible(true);
        subProgressBar.setVisible(true);
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
        if(! (fn instanceof  Root))
            putNormalInfo("Selected file/menu: " + fn.getPath());
        tableViewer.refreshWithFileNode(fn);
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
            runningThread = new Thread() {
                public void run() {
                    try {
                        root.addSource(f.getPath());
                    } catch (TarException e) {
                        Notifier.getNotifier().addNotifyMessage(new MSGAddedSource());
                        putErrorInfo(e.getMessage());
                        root = null;
                    }

                    // update
                    treeViewer.buildFromRoot(root);
                    setCurrentFileNode(root);
                    enableFrontEnd();
                }
            };
            disableFrontEnd();
            runningThread.start();
        }
    }

    void compress() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fc.setAcceptAllFileFilterUsed(false);

        int retVal = fc.showOpenDialog(this);

        if(retVal == JFileChooser.APPROVE_OPTION) {
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

            putNormalInfo("Starting compress ...");
            disableFrontEnd();
            runningThread = new Thread() {
                public void run() {
                    try {
                        root.compress(f.getAbsolutePath(), conf, 0);
                    } catch (TarException | DCException e) {
                        Notifier.getNotifier().addNotifyMessage(new MSGDumpedIndex());
                        putErrorInfo(e.getMessage());
                    }
                    enableFrontEnd();
                }
            };

            runningThread.start();
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
            File outputDir = fc.getSelectedFile();

            runningThread = new Thread() {
                public void run() {
                    try {
                        root.decompress(outputDir.getAbsolutePath(), fns, srcFile.getPath());
                    } catch (TarException | DCException e) {
                        Notifier.getNotifier().addNotifyMessage(new MSGDecompressDone());
                        putErrorInfo(e.getMessage());
                    }
                    enableFrontEnd();
                }
            };
            disableFrontEnd();
            runningThread.start();
        }
    }

    Config getConfig() {return conf;}

    void exit() {
        if(! checkRootUpdated()) {
            return ;
        }
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}
