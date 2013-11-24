package bin.ui.generic;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :      GenericInfoUI
 * Version :    ver 0.1
 * Usage :      the generic interface of InfoUI
 */
abstract public class GenericInfoUI extends GenericUI {
    protected String title, content;    // title and content

    /**
     * Set up the title
     * @param title title to show
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Set up the content
     * @param content the content to show
     */
    public void setContent(String content) {
        this.content = content;
    }
}
