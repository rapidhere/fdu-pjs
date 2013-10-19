package bin.ui.textbaseui;

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
    private static final TextBaseIOHandler ioh = new TextBaseIOHandler();
    private static TextBaseMenuUI menu_ui_instance = null;
    private static TextBaseMapUI map_ui_instance = null;

    public GenericMenuUI getMenuUI() {
        if(menu_ui_instance == null) {
            menu_ui_instance = new TextBaseMenuUI();
            menu_ui_instance.setIOHandler(ioh);
        }
        return menu_ui_instance;
    }

    public GenericMapUI getMapUI() {
        if(map_ui_instance == null) {
            map_ui_instance = new TextBaseMapUI();
            map_ui_instance.setIOHandler(ioh);
        }
        return map_ui_instance;
    }
}
