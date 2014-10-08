package bin.ui.textbaseui;

import bin.ui.generic.GenericInfoUI;
import bin.ui.generic.GenericMenuUI;
import bin.ui.generic.GenericUIFactory;
import bin.ui.generic.GenericMapUI;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :      TextBaseUIFactory
 * Version :    ver 0.1
 * Usage :      the text-based ui factory
 */

public class TextBaseUIFactory extends GenericUIFactory {
    private static TextBaseMenuUI menu_ui_instance = null;  // menu_ui
    private static TextBaseMapUI map_ui_instance = null;    // map_ui
    private static TextBaseInfoUI info_ui_instance = null;  // info_ui

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
