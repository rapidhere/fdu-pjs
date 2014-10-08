package bin.ui.generic;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :      GenericMenuUI
 * Version :    ver 0.1
 * Usage :      The Generic Interface of MenuUI
 */
abstract public class GenericMenuUI extends GenericUI {
    protected String[] menu_items;  // the items in menu
    protected String menu_title;    // the title of menu

    /**
     * Draw the menu title on the screen
     * @param title the title of menu
     */
    abstract protected void drawTitle(String title);

    /**
     * Draw the menu
     */
    abstract protected void drawMenu();

    /**
     * Return the choice of user
     * @return
     */
    abstract public int getChoice();

    /**
     * Constructor
     */
    public GenericMenuUI() {}

    /**
     * Set up the menu elements
     * @param menu_items list of String stand for the items of menu
     * @param menu_title the title of menu
     */
    public void setMenu(String[] menu_items, String menu_title) {
        this.menu_items = menu_items;
        this.menu_title = menu_title;
    }

    public void draw() {
        drawTitle(menu_title);
        drawMenu();
    }
}
