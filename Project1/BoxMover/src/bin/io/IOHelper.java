package bin.io;

import bin.Env;
import bin.exp.LevelNotFound;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class : IOHelper
 * Version : 0.1
 * Usage : Read the map file and give some utils to handle the map
 */

public class IOHelper {
    private int width, height;
    private char[][] map;

    public void readMap(int level)
    throws LevelNotFound {
        try {
            File map_file = new File(Env.getMap(level));
            Scanner input = new Scanner(map_file);

            width = input.nextInt();
            height = input.nextInt();

            map = new char[height][width];
            input.nextLine();
            for(int i = 0;i < height;i ++) {
                map[i] = input.nextLine().toCharArray();
            }
        } catch (FileNotFoundException e) {
            throw new LevelNotFound(level);
        }
    }

    public int getMapAt(int x, int y) {
        return map[x][y] - '0';
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    static Scanner input = new Scanner(System.in);
    static public char getChar() {
        while(true) {
            String buffer = input.nextLine();
            if(buffer.length() != 0) {
                return buffer.charAt(0);
            }
        }
    }

    static public char getLowerChar() {
        char ch = getChar();
        if(ch >= 'A' && ch <= 'Z') {
            ch = (char)('a' + ch - 'A');
        }
        return ch;
    }

    static public void putChar(char ch) {
        System.out.print(ch);
    }

    static public void putCharArray(char[] ch_arr) {
        for(int i = 0;i < ch_arr.length;i ++) {
            putChar(ch_arr[i]);
        }
    }

    static public void putEOL() {
        System.out.println();
    }

    static public void putStringLine(String s) {
        System.out.println(s);
    }

    static public void putError(String err_info) {
        putStringLine("Error: " + err_info);
    }
}
