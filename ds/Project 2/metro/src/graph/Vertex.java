package graph;

import java.util.ArrayList;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class Vertex {
    private String stationName;
    private int index;

    private int stationIndex;
    private int lineIndex;

    private boolean isExchange;

    private ArrayList<Edge> edges = new ArrayList<>();

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void addEdge(int v, Edge.CostT cost) {
        Edge ce = new Edge();
        ce.setCost(cost);
        ce.setV(v);
        this.edges.add(ce);
    }

    public int getStationIndex() {
        return stationIndex;
    }

    public void setStationIndex(int stationIndex) {
        this.stationIndex = stationIndex;
    }

    public int getLineIndex() {
        return lineIndex;
    }

    public void setLineIndex(int lineIndex) {
        this.lineIndex = lineIndex;
    }

    public void setExchange(boolean isExchange) {
        this.isExchange = isExchange;
    }
}
