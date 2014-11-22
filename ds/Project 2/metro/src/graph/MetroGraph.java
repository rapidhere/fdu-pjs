package graph;

import alg.Utils;

import java.util.*;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 */
public class MetroGraph {
    private Vertex[] V;
    private Map<Long, Integer> stationIndexMap = new HashMap<>();
    private int stationIndex = 0;
    private int stationIndexTable[][];

    public Vertex[] getG() {
        return V;
    }

    public void setG(Vertex[] v) {
        V = v;
    }

    public Vertex getVertex(int i) {
        return V[i];
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

    public Collection<Integer> getStationIndexes() {
        return stationIndexMap.values();
    }

    public void buildStationIndexTable() {
        ArrayList<Integer>[] sit = new ArrayList[stationIndex];

        for(int i = 0;i < sit.length;i ++)
            sit[i] = new ArrayList<>();

        for(int i = 0;i < V.length;i ++) {
            sit[V[i].getStationIndex()].add(i);
        }

        stationIndexTable = new int[stationIndex][];
        for(int i = 0;i < sit.length;i ++) {
            stationIndexTable[i] = new int[sit[i].size()];
            for(int j = 0;j < sit[i].size();j ++)
                stationIndexTable[i][j] = sit[i].get(j);
        }
    }

    public int[][] getStationIndexTable() {
        return stationIndexTable;
    }
}
