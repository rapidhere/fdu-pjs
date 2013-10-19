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
    abstract protected void drawSingleMenuItem(int index, String item);

    private String[] menu_items;
    private String menu_title;
    public GenericMenuUI() {}

    protected void drawMenu() {
        ioh.putEOL();
        for(int i = 0;i < menu_items.length;i ++) {
            drawSingleMenuItem(i, menu_items[i]);
        }
    }

    public void setMenu(String[] menu_items, String menu_title) {
        this.menu_items = menu_items;
        this.menu_title = menu_title;
    }

    public void draw() {
        drawTitle(menu_title);
        drawMenu();
    }

    public String getCommand() {
        ioh.putString("Please Choose:");
        return super.getCommand();
    }
}
