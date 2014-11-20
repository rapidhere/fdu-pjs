package alg;

import graph.Edge;
import graph.MetroGraph;
import graph.Vertex;

import java.util.*;


/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 */
public class ShortestPathWithMustPassVertex {
    public static Edge.CostT runShortestPathWithMustPassVertex(
        MetroGraph G, Edge.CostT rd[][], int[] midStations, int s, int t) {
        // get graph
        Vertex[] g = G.getG();

        // get all mid stations
        ArrayList<Integer> midList = new ArrayList<Integer>();
        for(int midIndex: midStations) {
            for(int i = 0;i < g.length;i ++)
                if(g[i].getStationIndex() == midIndex)
                    midList.add(i);
        }

        int mid[] = new int[midList.size()];
        for(int i = 0;i < midList.size();i ++)
            mid[i] = midList.get(i);

        // create dp function
        int m = midStations.length; // size of mid stations
        Edge.CostT[][] f = new Edge.CostT[mid.length][1 << m];
        boolean[][] vis = new boolean[mid.length][1 << m];

        // Map: from mid vertex index to array index
        int[] tm = new int[mid.length];
        for(int i = 0;i < mid.length;i ++) {
            for(int j = 0;j < midStations.length;j ++)
                if(g[mid[i]].getStationIndex() == midStations[j])
                    tm[i] = j;
        }

        // init queue
        Queue<Integer> queue = new LinkedList<Integer>();
        for(int i = 0; i < mid.length;i ++) {
            Edge.CostT cr = Edge.INFINITY_COST;
            for(int j = 0;j < g.length;j ++)
                if(g[j].getStationIndex() == s)
                    cr = Utils.min(cr, rd[j][mid[i]]);

            f[i][1 << tm[i]] = cr;
            queue.add((i << m) | (1 << tm[i]));
            vis[i][1 << tm[i]] = true;
        }

        // calc
        while(! queue.isEmpty()) {
            int u = queue.peek() >> m;
            int mask = queue.poll() & ((1 << m) - 1);

            for(int i = 0;i < mid.length;i ++)
            if((mask >> tm[i] & 1) == 0) {
                int cmask = mask | (1 << tm[i]);
                Edge.CostT cost = f[u][mask].add(rd[mid[u]][mid[i]]);
                if(f[i][cmask] == null || f[i][cmask].compareTo(cost) > 0) {
                    f[i][cmask] = cost;

                    if(! vis[i][cmask]) {
                        queue.add((i << m) | cmask);
                        vis[i][cmask] = true;
                    }
                }
            }
        }

        // move to end point
        Edge.CostT ret = Edge.INFINITY_COST;
        for(int i = 0;i < mid.length;i ++) {
            for(int j = 0;j < g.length;j ++)
                if(g[j].getStationIndex() == t)
                    ret = Utils.min(ret, f[i][(1 << mid.length) - 1].add(rd[mid[i]][j]));
        }

        // return
        return ret;
    }
}
