package core.dc;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */

public abstract class Token<T> implements Comparable<Token<T>> {
    private T token;

    public abstract int hashCode();

    @Override
    public boolean equals(Object o) {
        return o instanceof Token && this.hashCode() == o.hashCode();
    }

    public T getToken() {
        return token;
    }

    public void setToken(T o) {
        token = o;
    }
}