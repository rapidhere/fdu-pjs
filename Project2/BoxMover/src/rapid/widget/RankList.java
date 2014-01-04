package rapid.widget;

import java.io.*;
import java.util.*;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class : RankList
 * Version : 0.1
 * Usage : The rank list
 */
public class RankList implements Serializable {
    // a single record of rank
    public class RankItem {
        public String name;
        public int score;

        RankItem(String name, int score) {
            this.name = name;
            this.score = score;
        }
    }

    // store in a hash map
    private HashMap<String, Integer> rec = new HashMap<String, Integer>();

    // singleton
    private static RankList _instance = null;
    public static RankList get() {
        if(_instance != null) {
            return _instance;
        }

        // load rank list
        File f = new File("./" + "rank.dat");
        if(f.isFile()) {
            try {
                ObjectInputStream oin = new ObjectInputStream(new FileInputStream(f));
                return _instance = (RankList)oin.readObject();
            } catch(Exception e) {
                e.printStackTrace();
            }
        } else {
            return _instance = new RankList();
        }
        return null;
    }

    private RankList() {}

    // save rank list
    public void save() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./" + "rank.dat"));
            oos.writeObject(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // update rank
    public void update(String userName, int score) {
        rec.put(userName, score);
    }

    // get the score of user
    public int getScore(String userName) {
        return rec.get(userName);
    }

    // get top three rank
    public RankItem[] getTopThree() {
        int size = 3;
        if(rec.size() < size) {
            size = rec.size();
        }

        RankItem ret[] = new RankItem[size];
        ArrayList<Map.Entry<String, Integer>> l = new ArrayList<Map.Entry<String, Integer>>(rec.entrySet());
        Collections.sort(l, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                if(!o2.getValue().equals(o1.getValue())) {
                    return (o2.getValue() - o1.getValue());
                }

                return o2.getKey().compareTo(o1.getKey());
            }
        });

        for(int i = 0;i < size;i ++) {
            ret[i] = new RankItem(l.get(i).getKey(), l.get(i).getValue());
        }

        return ret;
    }
}
