package rapid.ui;

import rapid.Env;
import rapid.ctrl.CommandId;
import rapid.ctrl.GameCommandEvent;
import rapid.widget.BMContainer;
import rapid.widget.BMMap;
import rapid.widget.BMWidget;
import rapid.widget.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class GamePanel extends GenericPanel {
    private User currentUser;
    private BMMap map;

    public GamePanel(User currentUser) {
        this.currentUser = currentUser;
        this.map = this.currentUser.getMap();

        this.setSize(map.getWidth() * Env.BLOCK_SIZE, map.getHeight() * Env.BLOCK_SIZE);

        final GamePanel self = this;

        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_W:
                        gl.onKeyUp(new GameCommandEvent(CommandId.MOVE_UP, null, self));
                        break;

                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_S:
                        gl.onKeyDown(new GameCommandEvent(CommandId.MOVE_DOWN, null, self));
                        break;

                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_A:
                        gl.onKeyLeft(new GameCommandEvent(CommandId.MOVE_LEFT, null, self));
                        break;

                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_D:
                        gl.onKeyRight(new GameCommandEvent(CommandId.MOVE_RIGHT, null, self));
                        break;
                }
            }
        });
    }

    public void paintComponent(Graphics g) {
        removeAll();
        for(int i = 0;i < map.getHeight();i ++) {
            for(int j = 0;j < map.getWidth();j ++) {
                JLabel cur = createBlock(i, j);

                add(cur);
                cur.setLocation(j * Env.BLOCK_SIZE, i * Env.BLOCK_SIZE);
            }
        }
    }

    private JLabel createBlock(int i, int j) {
        BMWidget w = map.getWidgetAt(i, j);
        String icoFile;

        switch (w.getTypeId()) {
            case BMWidget.BLOCK_TYPE_BOX:
            case BMWidget.BLOCK_TYPE_NULL:
            case BMWidget.BLOCK_TYPE_WALL:
                icoFile = w.getTypeId() + ".png";
                break;
            case BMWidget.BLOCK_TYPE_EMPTY:
                if(w.isPassable()) {
                    icoFile = "2.png";
                } else {
                    icoFile = ((BMContainer)w).getInner().getTypeId() + ".png";
                }
                break;
            case BMWidget.BLOCK_TYPE_TARGET:
                if(w.isPassable()) {
                    icoFile = "4.png";
                } else {
                    if(((BMContainer)w).getInner().getTypeId() == BMWidget.BLOCK_TYPE_BOX) {
                        icoFile = "6.png";
                    } else {
                        icoFile = ((BMContainer)w).getInner().getTypeId() + ".png";
                    }
                }
                break;
            default:
                icoFile = "";
        }

        ImageIcon ico = new ImageIcon(Env.PIC_DIRECTORY + icoFile);
        JLabel ret = new JLabel(ico);
        ret.setSize(Env.BLOCK_SIZE, Env.BLOCK_SIZE);

        return ret;
    }
}