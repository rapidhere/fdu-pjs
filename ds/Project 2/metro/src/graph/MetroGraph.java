package graph;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 */
public class MetroGraph {
    private Vertex[] V;
    private Map<String, Integer> stationIndexMap = new HashMap<String, Integer>();
    private int stationIndex = 0;


    public Vertex[] getG() {
        return V;
    }

    public void setG(Vertex[] v) {
        V = v;
    }

    public int getStationIndex(String station) {
        return stationIndexMap.get(station);
    }

    public void putStation(String station) {
        assert stationIndexMap.get(station) == null;
        stationIndexMap.put(station, stationIndex ++);
    }
}
