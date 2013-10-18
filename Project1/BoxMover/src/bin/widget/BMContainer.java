package bin.widget;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
abstract public class BMContainer extends BMWidget {
    BMMovable inner = null;

    public BMContainer(int type_id, int x, int y, char token) {
        super(type_id, x, y, token);
    }

    public boolean isPassable() {
        return inner == null;
    }

    public void putIn(BMMovable w) {
        inner = w;
        inner.moveTo(getX(), getY());
    }

    public void putOut() {
        inner = null;
    }

    public char getToken() {
        if(inner != null) {
            return inner.getToken();
        } else {
            return super.getToken();
        }
    }

    public BMMovable getInner() {
        return inner;
    }
}
