package bin.ui.generic;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
abstract public class GenericMenuUI extends GenericUI {
    abstract protected void drawTitle(String title);

    protected String[] menu_items;
    protected String menu_title;
    public GenericMenuUI() {}

    abstract protected void drawMenu();

    public void setMenu(String[] menu_items, String menu_title) {
        this.menu_items = menu_items;
        this.menu_title = menu_title;
    }

    public void draw() {
        drawTitle(menu_title);
        drawMenu();
    }

    abstract public int getChoice();
}
