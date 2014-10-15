package ui.gui;

import core.tar.FileNode;
import core.tar.Menu;
import core.tar.RegularFile;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.metal.MetalIconFactory;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class RMenuTableViewer extends JTable {
    private String[] cNames = {" ", "File Name", "Size", "C-Size"};
    DefaultTableModel tm;
    private FileNode[] currentFns;

    public RMenuTableViewer() {
        // set model
        tm = new DefaultTableModel(null, cNames) {
            @Override
            public Class<?> getColumnClass(int index) {
                switch(index) {
                    case 0: return Icon.class;
                    case 1: return String.class;
                    case 2: return Integer.class;
                    case 3: return Integer.class;
                }
                return null;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        setModel(tm);

        // set width
        getColumn(cNames[0]).setMinWidth(50);
        getColumn(cNames[0]).setMaxWidth(50);

        getColumn(cNames[1]).setMinWidth(200);

        getColumn(cNames[2]).setMinWidth(100);
        getColumn(cNames[2]).setPreferredWidth(100);
        getColumn(cNames[2]).setMaxWidth(200);

        getColumn(cNames[3]).setMaxWidth(100);
        getColumn(cNames[3]).setPreferredWidth(100);
        getColumn(cNames[3]).setMinWidth(200);

        // set auto resizable
        setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // set table row height
        setRowHeight(20);

        // update
        invalidate();

        // set popup menu
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem;
        setComponentPopupMenu(popupMenu);

        // decompress
        menuItem = new JMenuItem("decompress");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedIndex = getSelectedRows();
                FileNode[] fns = new FileNode[selectedIndex.length];

                for(int i = 0;i <selectedIndex.length;i ++) {
                    fns[i] = currentFns[selectedIndex[i]];
                }
                RFrame.getFrame().decompress(fns);
            }
        });
        popupMenu.add(menuItem);
    }

    void refreshWithFileNode(FileNode fn) {
        // clear up
        while(tm.getRowCount() != 0)
            tm.removeRow(0);

        if(fn instanceof RegularFile) {
            Object[] data=
                {new MetalIconFactory.TreeLeafIcon(), fn.getName(), fn.getSize(), fn.getCompressedSize()};
            tm.addRow(data);
            return ;
        }

        FileNode[] children = ((Menu)fn).getChildren();
        Arrays.sort(children);
        for(FileNode f: children) {
            Icon ico;
            if(f instanceof Menu)
                ico = new MetalIconFactory.TreeFolderIcon();
            else
                ico = new MetalIconFactory.TreeLeafIcon();

            Object[] data = {ico, f.getName(), f.getSize(), f.getCompressedSize()};
            tm.addRow(data);
        }
        currentFns = children;
    }
}
