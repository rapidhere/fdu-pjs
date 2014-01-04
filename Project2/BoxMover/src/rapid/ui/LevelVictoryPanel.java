package rapid.ui;

import rapid.Env;
import rapid.ctrl.GameCommandEvent;
import rapid.widget.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class : LevelVictoryPanel
 * Version : 0.1
 * Usage : show after win a level
 */
public class LevelVictoryPanel extends InfoPanel {
    private User currentUser;
    // level, steps, back steps, time, score
    private int lv, mv, bm, ti, sc;

    public LevelVictoryPanel(User _currentUser) {
        super(Env.PIC_DIRECTORY + "Win.png", Env.PIC_DIRECTORY + "continue.png", false);

        this.currentUser = _currentUser;

        int cLevel = currentUser.getMap().getLevel();

        // get data
        lv = currentUser.getMap().getLevel();
        mv = currentUser.getMap().getCurrentStep();
        bm = currentUser.getMap().getBackStep();
        ti = currentUser.getMap().getTime();
        sc = currentUser.getMap().getScore();

        // set next level
        if(cLevel == 9) {
            cLevel = 1;
        } else {
            cLevel ++;
        }
        try {
            currentUser.getMap().loadLevel(cLevel);
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        final LevelVictoryPanel self = this;

        // back to level on click
        bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            gl.onBackToLevel(new GameCommandEvent(null, self));
            }
        });
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setFont(new Font(null, Font.PLAIN, 16));
        g.setColor(Color.red);

        // draw info
        g.drawString(currentUser.getName(), 200, 95);
        g.drawString("" + lv, 200, 140);
        g.drawString("" + mv, 200, 180);
        g.drawString("" + bm, 200, 222);
        g.drawString(String.format("%02d:%02d", ti / 60, ti % 60), 200, 264);
        g.drawString("" + sc, 200, 308);
    }
}
