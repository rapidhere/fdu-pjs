package rapid.widget;

import rapid.Env;
import sun.awt.image.PixelConverter;

import java.io.*;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class User {
    private BMMap map;
    private String name;

    public BMMap getMap() {
        return map;
    }

    public String getName() {
        return name;
    }

    public void save() {
        ObjectOutputStream out;

        try {
            out = new ObjectOutputStream(new FileOutputStream(Env.USR_DIRECTORY + name));
            out.writeObject(map);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void read() {
        ObjectInputStream oin;

        try {
            oin = new ObjectInputStream(new FileInputStream(Env.USR_DIRECTORY + name));
            map = (BMMap)oin.readObject();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static User newUser(String name) {
        User ret = new User();

        ret.map = new BMMap();
        ret.name = name;

        RankList rl = RankList.get();
        rl.update(ret.name, 0);

        try {
            ret.map.loadLevel(1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ret.save();
        return ret;
    }

    public static User getUser(String name) {
        User ret = new User();

        ret.name = name;
        ret.read();

        return ret;
    }

    public int getScore() {
        RankList rl = RankList.get();
        return rl.getScore(this.name);
    }

    public void incScore(int delta) {
        RankList rl = RankList.get();
        rl.update(this.name, rl.getScore(name) + delta);
    }

    private User() {
    }
}
