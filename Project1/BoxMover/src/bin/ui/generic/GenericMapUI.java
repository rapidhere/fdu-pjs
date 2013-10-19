package bin.ui.generic;

import bin.widget.BMMap;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
abstract public class GenericMapUI extends GenericUI {
    BMMap map;
    public void setMap(BMMap map) {
        this.map = map;
    }

    public void draw() {
        for(int i = 0;i < map.getHeight();i ++) {
            for(int j = 0;j < map.getWidth();j ++) {
                ioh.putToken(map.getWidgetAt(i, j).getToken());
            }
            ioh.putEOL();
        }
    }
}
