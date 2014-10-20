package ui.gui;

import core.tar.FileNode;
import core.tar.Menu;
import core.tar.RegularFile;
import core.tar.Root;

import javax.swing.*;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class RMenuTreeViewer extends JTree {
    public RMenuTreeViewer() {
        super((TreeNode)null);

        // can see root
        setRootVisible(true);

        // set render
        setCellRenderer(new RFileNodeRenderer());

        // not editable
        setEditable(false);

        // add mouse listener
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TreePath selPath = getPathForLocation(e.getX(), e.getY());
                if (selPath == null)
                    return;
                if (e.getClickCount() == 1) {
                    FileNode fn = (FileNode) selPath.getLastPathComponent();
                    RFrame.getFrame().setCurrentFileNode(fn);
                }
            }
        });
    }

    public void buildFromRoot(Root root) {
        TreeModel newRoot = new RRootAdaptor(root);
        setModel(newRoot);
    }
}

class RRootAdaptor implements TreeModel {
    private Root root;
    public RRootAdaptor(Root root) {
        this.root = root;
    }

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        FileNode[] fns = ((Menu)parent).getChildren();
        Arrays.sort(fns);
        return fns[index];
    }

    @Override
    public int getChildCount(Object parent) {
        return ((Menu)parent).getChildren().length;
    }

    @Override
    public boolean isLeaf(Object node) {
        return node instanceof RegularFile;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        // do nothing
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        FileNode[] fns = ((Menu)parent).getChildren();
        for(int i = 0;i < fns.length;i ++) {
            if(child.equals(fns[i]))
                return i;
        }
        return -1;
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        // do nothing
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        // do nothing
    }
}

class RFileNodeRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(
        JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, ((FileNode)value).getName(), selected, expanded, leaf, row, hasFocus);
        return this;
    }
}
