package bin.ui.textbaseui;

import bin.ui.generic.GenericMenuUI;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class TextBaseMenuUI extends GenericMenuUI {
    protected void drawTitle(String title) {
        ioh.putStringLine(title);
    }

    protected void drawSingleMenuItem(int index, String item) {
        ioh.putStringLine(index + ". " +item);
    }
}
