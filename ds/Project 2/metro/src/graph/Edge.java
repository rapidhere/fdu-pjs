package graph;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 */
public class Edge {
    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public CostT getCost() {
        return cost;
    }

    public void setCost(CostT cost) {
        this.cost = cost;
    }

    public static class CostT implements Comparable<CostT>{
        public int time, change;

        public CostT() {
            this(0, 0);
        }

        public CostT(int t, int c) {
            time = t;
            change = c;
        }

        @Override
        public int compareTo(CostT o) {
            return time * 100000 + change - o.time * 100000 - o.change;
        }

        public CostT add(CostT b) {
            return new CostT(time + b.time, change + b.change);
        }
    }

    public static final CostT INFINITY_COST = new CostT(0x3f3f3f3f, 0x3f3f3f3f);

    private int v;
    private CostT cost;
}
