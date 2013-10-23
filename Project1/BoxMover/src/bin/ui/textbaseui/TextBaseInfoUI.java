package bin.ui.textbaseui;

import bin.io.IOHelper;
import bin.ui.generic.GenericInfoUI;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class TextBaseInfoUI extends GenericInfoUI {
    public void draw() {
        IOHelper.putSolidLine();
        IOHelper.putStringLine(title);
        IOHelper.putStringLine(content);
        IOHelper.putSolidLine();
    }
}
