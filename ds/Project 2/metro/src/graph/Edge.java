package graph;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class Edge {
    public int getV() {
        return v;
    }

    public void setV(int dest) {
        this.v = dest;
    }

    public CostT getCost() {
        return cost;
    }

    public void setCost(CostT cost) {
        this.cost = cost;
    }

    public static class CostT implements Comparable<CostT>{
        @Override
        public int compareTo(CostT o) {
            return 0;
        }

        public CostT add(CostT b) {
            return this;
        }
    }

    public static final CostT INFINITY_COST = new CostT();

    private int v;
    private CostT cost;
}
