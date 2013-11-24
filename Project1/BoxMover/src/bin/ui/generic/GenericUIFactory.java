package bin.ui.generic;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :      GenericUIFactory
 * Version :    ver 0.1
 * Usage :      The Generic Factory Interface to generate UIs
 *              See Developer manual for more info
 */

abstract public class GenericUIFactory {
    /**
     * Get the MapUI
     * @return
     */
    public abstract GenericMapUI getMapUI();

    /**
     * Get the MenuUI
     * @return
     */
    public abstract GenericMenuUI getMenuUI();

    /**
     * Get the InfoUI
     * @return
     */
    public abstract GenericInfoUI getInfoUI();
}
