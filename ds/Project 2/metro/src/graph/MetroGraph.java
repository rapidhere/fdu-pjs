package graph;

import alg.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 */
public class MetroGraph {
    private Vertex[] V;
    private Map<Long, Integer> stationIndexMap = new HashMap<>();
    private int stationIndex = 0;


    public Vertex[] getG() {
        return V;
    }

    public void setG(Vertex[] v) {
        V = v;
    }

    public int getStationIndex(String station) {
        return stationIndexMap.get(Utils.hashString(station));
    }

    public void putStation(String station) {
        long h = Utils.hashString(station);
        if(stationIndexMap.get(h) == null) {
            stationIndexMap.put(h, stationIndex ++);
        }
    }
}
