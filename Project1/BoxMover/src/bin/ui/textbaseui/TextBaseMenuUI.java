package bin.ui.textbaseui;

import bin.io.IOHelper;
import bin.ui.generic.GenericMenuUI;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class TextBaseMenuUI extends GenericMenuUI {
    private int choice;

    protected void drawTitle(String title) {
        IOHelper.putStringLine(title);
    }

    protected void drawMenu() {
        for(int i = 0;i < menu_items.length;i ++) {
            IOHelper.putStringLine(i + ". " + menu_items[i]);
        }
    }

    public void draw() {
        IOHelper.putSolidLine();
        super.draw();
        while (true) {
            try {
                IOHelper.putStringLine("Please Chose: ");
                choice = Integer.parseInt(IOHelper.getLowerStringLine());
                if(choice >= 0 && choice < menu_items.length) {
                    break;
                }
            } catch (Exception e) {
                continue;
            }
        }
        IOHelper.putSolidLine();
    }

    public int getChoice() {
        return choice;
    }
}
