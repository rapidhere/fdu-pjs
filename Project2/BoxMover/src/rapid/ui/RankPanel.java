package rapid.ui;

import rapid.Env;
import rapid.widget.RankList;

import java.awt.*;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class : RankPanel
 * Version : 0.1
 * Usage : The RankPanel
 */
public class RankPanel extends InfoPanel {
    public RankPanel() {
        super(Env.PIC_DIRECTORY + "Rank.png", Env.PIC_DIRECTORY + "back.png", true);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // draw rank
        RankList.RankItem rl[] = RankList.get().getTopThree();

        g.setFont(new Font(null, Font.PLAIN, 16));
        g.setColor(Color.black);

        for(int i = 0;i < rl.length;i ++) {
            g.drawString(rl[i].name, 170, 140 + i * 60);
            g.drawString("" + rl[i].score, 300, 140 + i * 60);
        }
    }
}
