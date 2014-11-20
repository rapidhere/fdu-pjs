package alg;

import graph.Edge;
import graph.MetroGraph;
import graph.Vertex;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 */
public class Floyd {
    static public Edge.CostT[][] runFloyd(MetroGraph G) {
        Vertex[] g = G.getG();
        Edge.CostT[][] dist = new Edge.CostT[g.length][g.length];

        for(int i = 0;i < g.length;i ++)
        for(int j = 0;j < g.length;j ++)
            dist[i][j] = Edge.INFINITY_COST;

        for(Vertex v : g) {
            Edge[] es = v.getEdges();
            for(Edge e : es)
                dist[v.getIndex()][e.getV()] = e.getCost();
        }

        for(int k = 0;k < g.length;k ++)
        for(int i = 0;i < g.length;i ++)
        for(int j = 0;j < g.length;j ++) {
            dist[i][j] = Utils.min(dist[i][j], dist[i][k].add(dist[k][j]));
        }

        return dist;
    }
}
