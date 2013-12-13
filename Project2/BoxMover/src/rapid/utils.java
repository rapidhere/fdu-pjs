package rapid;

import javax.swing.*;
import java.awt.*;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class utils {
    public static JButton createButton(String path) {
        JButton bt = new JButton(new ImageIcon(path));
        bt.setMargin(new Insets(0, 0, 0, 0));
        bt.setBorder(BorderFactory.createEtchedBorder());
        bt.setContentAreaFilled(false);

        return bt;
    }
}
