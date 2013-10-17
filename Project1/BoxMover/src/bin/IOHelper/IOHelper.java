package bin.IOHelper;

import bin.Env;
import bin.Exception.LevelNotFound;

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

            map = new char[height][];
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
    static char getChar() {
        return (char)input.nextByte();
    }

    static char getLowerChar() {
        char ch = getChar();
        if(ch >= 'A' && ch <= 'Z') {
            ch = (char)('a' + ch - 'A');
        }
        return ch;
    }
}
