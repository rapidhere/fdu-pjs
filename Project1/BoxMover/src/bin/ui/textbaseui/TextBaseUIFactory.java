package bin.ui.textbaseui;

import bin.ui.generic.GenericInfoUI;
import bin.ui.generic.GenericMenuUI;
import bin.ui.generic.GenericUIFactory;
import bin.ui.generic.GenericMapUI;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */

public class TextBaseUIFactory extends GenericUIFactory {
    private static TextBaseMenuUI menu_ui_instance = null;
    private static TextBaseMapUI map_ui_instance = null;
    private static TextBaseInfoUI info_ui_instance = null;

    public GenericMenuUI getMenuUI() {
        if(menu_ui_instance == null) {
            menu_ui_instance = new TextBaseMenuUI();
        }
        return menu_ui_instance;
    }

    public GenericMapUI getMapUI() {
        if(map_ui_instance == null) {
            map_ui_instance = new TextBaseMapUI();
        }
        return map_ui_instance;
    }

    public GenericInfoUI getInfoUI() {
        if(info_ui_instance == null) {
            info_ui_instance = new TextBaseInfoUI();
        }
        return info_ui_instance;
    }
}
