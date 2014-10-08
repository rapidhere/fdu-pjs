package rapid.ui;

import rapid.Env;
import rapid.ctrl.GameCommandEvent;
import rapid.widget.BMContainer;
import rapid.widget.BMMap;
import rapid.widget.BMWidget;
import rapid.widget.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class : GamePanel
 * Version : 0.1
 * Usage : The playing panel
 */
public class GamePanel extends GenericPanel {
    private User currentUser; // current User
    private BMMap map; // the map
    private Timer timer;  // the timer

    public GamePanel(User currentUser) {
        this.currentUser = currentUser;
        this.map = this.currentUser.getMap();

        // set up timer
        this.timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                map.incTime();
                repaint();
            }
        });
        timer.start();

        this.setSize(map.getWidth() * Env.BLOCK_SIZE, map.getHeight() * Env.BLOCK_SIZE);

        final GamePanel self = this;

        // listen to keyboard
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_W:
                        gl.onKeyUp(new GameCommandEvent(null, self));
                        repaint();
                        break;

                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_S:
                        gl.onKeyDown(new GameCommandEvent(null, self));
                        repaint();
                        break;

                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_A:
                        gl.onKeyLeft(new GameCommandEvent(null, self));
                        repaint();
                        break;

                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_D:
                        gl.onKeyRight(new GameCommandEvent(null, self));
                        repaint();
                        break;
                    case KeyEvent.VK_BACK_SPACE:
                    case KeyEvent.VK_B:
                        Object args[] = {1};
                        gl.onBackStep(new GameCommandEvent(args, self));
                        repaint();
                        break;
                }
            }
        });
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // draw the blocks
        for(int i = 0;i < map.getHeight();i ++) {
            for(int j = 0;j < map.getWidth();j ++) {
                ImageIcon cur = createBlock(i, j);
                g.drawImage(cur.getImage(), j * Env.BLOCK_SIZE, i * Env.BLOCK_SIZE, this);
            }
        }

        // draw the time
        g.setFont(new Font("Consolas", Font.PLAIN, 30));
        g.setColor(Color.red);

        g.drawString(String.format("Time:%02d:%02d", map.getTime() / 60, map.getTime() % 60), (getWidth() - 160) / 2, 30);
        g.drawString(String.format("Steps:% 4d", map.getCurrentStep()), (getWidth() - 160) / 2, 70);
    }

    // how to create a block
    private ImageIcon createBlock(int i, int j) {
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
                if(w.isPassable()) { // empty
                    icoFile = "4.png";
                } else {
                    if(((BMContainer)w).getInner().getTypeId() == BMWidget.BLOCK_TYPE_BOX) {
                        icoFile = "6.png"; // filled with box
                    } else {
                        icoFile = ((BMContainer)w).getInner().getTypeId() + ".png";
                    }
                }
                break;
            default:
                icoFile = "";
        }

        return new ImageIcon(Env.PIC_DIRECTORY + icoFile);
    }

    // stop timer
    public void destroy() {
        timer.stop();
    }
}