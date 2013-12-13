package rapid.ui;

import rapid.Env;
import rapid.ctrl.CommandId;
import rapid.ctrl.GameCommandEvent;
import rapid.widget.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class LevelVictoryPanel extends InfoPanel {
    private User currentUser;

    public LevelVictoryPanel(User _currentUser) {
        super(Env.PIC_DIRECTORY + "Win.png", Env.PIC_DIRECTORY + "continue.png", false);

        this.currentUser = _currentUser;

        final LevelVictoryPanel self = this;
        bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int cLevel = currentUser.getMap().getLevel();

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

                gl.onBackToLevel(new GameCommandEvent(CommandId.BACK_TO_LEVEL, null, self));
            }
        });
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setFont(new Font(null, Font.PLAIN, 16));
        g.setColor(Color.red);

        int lv = currentUser.getMap().getLevel();
        int mv = currentUser.getMap().getCurrentStep();
        int bm = currentUser.getMap().getBackStep();
        int ti = currentUser.getMap().getTime();
        int sc = 1000 * lv - mv * 10 - bm * 50 - ti;

        g.drawString(currentUser.getName(), 200, 100);
        g.drawString("" + lv, 200, 130);
        g.drawString("" + mv, 200, 160);
        g.drawString("" + bm, 200, 190);
        g.drawString(String.format("%02d:%02d", ti / 60, ti % 60), 200, 210);
        g.drawString("" + sc, 200, 240);
    }
}
