package bin.exp;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */

public class ArgumentError extends BMException {
    private String ex_info;

    public ArgumentError(String ex_info) {
        this.ex_info = ex_info;
    }

    public String getExString() {
        return ex_info;
    }
}
