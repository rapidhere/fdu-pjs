package alg;

import graph.Edge;
import graph.MetroGraph;
import graph.Vertex;

import java.util.PriorityQueue;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 */
public class AllPairShortestPath {
    // run floyd
    static public Edge.CostT[][] runFloyd(MetroGraph G) {
        Vertex[] g = G.getG();
        Edge.CostT[][] dist = new Edge.CostT[g.length][g.length];

        for(int i = 0;i < g.length;i ++) {
            for (int j = 0; j < g.length; j++) {
                dist[i][j] = Edge.INFINITY_COST;
            }
        }

        for(int i = 0;i < g.length;i ++)
            dist[i][i] = new Edge.CostT(0, 0);

        for(Vertex v : g) {
            for(Edge e :  v.getEdges())
                dist[v.getIndex()][e.getV()] = e.getCost();
        }

        for(int k = 0;k < g.length;k ++)
        for(int i = 0;i < g.length;i ++)
        for(int j = 0;j < g.length;j ++) {
            Edge.CostT cost = dist[i][k].add(dist[k][j]);
            int test = dist[i][j].compareTo(cost);
            if(test > 0) {
                dist[i][j] = cost;
            }
        }

        return dist;
    }

    // use dijkstra
    static public Edge.CostT[][] runDijkstra(MetroGraph G) {
        Vertex[] g = G.getG();
        Edge.CostT[][] ret = new Edge.CostT[g.length][g.length];

        for(int i = 0;i < g.length;i ++) {
            doDijkstra(ret[i], g, i);
        }

        return ret;
    }

    static class Pair implements Comparable<Pair>{
        Edge.CostT dist;
        int v;

        @Override
        public int compareTo(Pair o) {
            return dist.compareTo(o.dist);
        }

        public Pair(){
            this(new Edge.CostT(), 0);
        }

        public Pair(Edge.CostT d, int v) {
            dist = d;
            this.v = v;
        }
    }

    static private void doDijkstra(Edge.CostT[] dist, Vertex[] g, int s) {
        for(int i = 0;i < dist.length;i ++)
            dist[i] = Edge.INFINITY_COST;
        dist[s] = new Edge.CostT();

        boolean vis[] = new boolean[g.length];
        PriorityQueue<Pair> heap = new PriorityQueue<>();
        heap.add(new Pair(dist[s], s));

        while(! heap.isEmpty()) {
            Edge.CostT d;
            int u;
            while(true) {
                d = heap.peek().dist;
                u = heap.poll().v;
                if(! vis[u])
                    break;
            }
            vis[u] = true;

            for(Edge e: g[u].getEdges()) {
                if (!vis[e.getV()] && dist[e.getV()].compareTo(d.add(e.getCost())) > 0) {
                    dist[e.getV()] = d.add(e.getCost());
                    heap.add(new Pair(dist[e.getV()], e.getV()));
                }
            }
        }
    }
}
