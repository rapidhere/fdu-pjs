package ui.gui;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class RMenuTableViewer extends JTable {
    private String[] cNames = {" ", "File Name", "Size", "C-Size"};

    public RMenuTableViewer() {
        // set model
        DefaultTableModel tm = new DefaultTableModel(null, cNames) {
            @Override
            public Class<?> getColumnClass(int index) {
                switch(index) {
                    case 0: return Boolean.class;
                    case 1: return String.class;
                    case 2: return Integer.class;
                    case 3: return Integer.class;
                }
                return null;
            }
        };
        setModel(tm);

        // set width
        getColumn(cNames[0]).setMaxWidth(50);
        getColumn(cNames[0]).setMinWidth(50);

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
    }
}
