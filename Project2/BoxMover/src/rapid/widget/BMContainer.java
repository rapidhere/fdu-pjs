package rapid.widget;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :      BMContainer
 * Version :    ver 0.1
 * Usage :      The widget that can contain a movable widget in the map
 */
abstract public class BMContainer extends BMWidget {
    BMMovable inner = null;     // The inner widget of container

    /**
     * Constructor
     * @param type_id the type_id of widget, see ENV.BLOCK_TYPE_*
     * @param x position x
     * @param y position y
     */
    public BMContainer(int type_id, int x, int y) {
        super(type_id, x, y);
    }

    public boolean isPassable() {
        return inner == null;
    }

    /**
     * Put a Movable into Container, assume that Container is Empty
     * @param w put Movable widget w into Container
     */
    public void putIn(BMMovable w) {
        inner = w;
        inner.moveTo(getX(), getY());
    }

    /**
     * Set the container to empty
     */
    public void putOut() {
        inner = null;
    }

    /**
     * get The inner Widget of Container
     * @return  the inner widget
     */
    public BMMovable getInner() {
        return inner;
    }
}
