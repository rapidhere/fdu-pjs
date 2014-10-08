package excs;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class FirstChuckSizeTooSmall extends DCUpdateException {
    public FirstChuckSizeTooSmall(int size, int requireSize) {
        super("First update size is too small: give " + size + " require " + requireSize);
    }
}
