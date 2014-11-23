package alg;

import graph.Edge;
import graph.MetroGraph;
import graph.PathList;
import javafx.util.Pair;

import java.util.*;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 */
public class ShortestPathWithMustPassVertex {
    public static Pair<Edge.CostT, ArrayList<PathList>> runShortestPathWithMustPassVertex(
        MetroGraph G, Edge.CostT rd[][], int[] midStations, int s, int t) {

        // get Station Index table
        int[][] sit = G.getStationIndexTable();

        // no mid stations
        if (midStations.length == 0) {
            Edge.CostT ret = Edge.INFINITY_COST;

            for (int i = 0; i < sit[s].length; i++) {
                for (int j = 0; j < sit[t].length; j++) {
                    int u = sit[s][i], v = sit[t][j];
                    if(ret.compareTo(rd[u][v]) > 0) {
                        ret = rd[u][v];
                    }
                }
            }

            ArrayList<PathList> rec = new ArrayList<>();
            PathList tp = new PathList();
            tp.add(t); tp.add(s);
            rec.add(tp);
            return new Pair<>(ret, rec);
        }

        // create dp function
        int m = midStations.length; // size of mid stations
        Edge.CostT[][] f = new Edge.CostT[m][1 << m];
        boolean[][] vis = new boolean[m][1 << m];

        // get all mid stations shortest path
        Edge.CostT[][] msp = new Edge.CostT[m][m];
        for(int i = 0;i < midStations.length;i ++) {
            for(int j = 0;j < midStations.length;j ++) {
                if (i == j) {
                    msp[i][j] = new Edge.CostT();
                    continue;
                }

                msp[i][j] = Edge.INFINITY_COST;
                for(int ii = 0;ii < sit[midStations[i]].length;ii ++)
                    for(int jj = 0;jj < sit[midStations[j]].length;jj ++)
                        msp[i][j] = Utils.min(msp[i][j], rd[sit[midStations[i]][ii]][sit[midStations[j]][jj]]);
            }
        }

        // to start point
        Edge.CostT[] smp = new Edge.CostT[m];
        for(int i = 0;i < m;i ++) {
            smp[i] = Edge.INFINITY_COST;
            for(int j = 0;j < sit[s].length;j ++)
                for(int k = 0;k < sit[midStations[i]].length;k ++){
                    int u = sit[s][j], v = sit[midStations[i]][k];
                    smp[i] = Utils.min(smp[i], rd[u][v]);
                }
        }

        // to end point
        Edge.CostT[] mep = new Edge.CostT[m];
        for(int i = 0;i < m;i ++) {
            mep[i] = Edge.INFINITY_COST;
            for(int j = 0;j < sit[t].length;j ++)
                for(int k = 0;k < sit[midStations[i]].length;k ++){
                    int u = sit[t][j], v = sit[midStations[i]][k];
                    mep[i] = Utils.min(mep[i], rd[v][u]);
                }
        }

        // init queue
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < m; i ++) {
            f[i][1 << i] = smp[i];
            queue.add((i << m) | (1 << i));
            vis[i][1 << i] = true;
        }

        // calc
        while (!queue.isEmpty()) {
            int u = queue.peek() >> m;
            int mask = queue.poll() & ((1 << m) - 1);

            for (int i = 0; i < m; i++)
                if ((mask >> i & 1) == 0) {
                    int cmask = mask | (1 << i);
                    Edge.CostT cost = f[u][mask].add(msp[u][i]);

                    if (f[i][cmask] == null || f[i][cmask].compareTo(cost) > 0) {
                        f[i][cmask] = cost;

                        if (!vis[i][cmask]) {
                            queue.add((i << m) | cmask);
                            vis[i][cmask] = true;
                        }
                    }
                }
        }

        // move to end point
        Edge.CostT ret = Edge.INFINITY_COST;
        for (int i = 0; i < m; i++) {
            ret = Utils.min(ret, f[i][(1 << m) - 1].add(mep[i]));
        }

        // build path
        ArrayList<PathList> rec = new ArrayList<>();
        for(int i = 0;i < m;i ++) {
            // this is the ret path
            if(f[i][(1 << m) - 1].add(mep[i]).compareTo(ret) == 0) {
                PathList cpath = new PathList();
                cpath.add(t);
                buildPath(i << m | ((1 << m) - 1), rec, cpath, m, m, f, msp, midStations, s);
            }
        }

        // add addition exchange
        ret.change += midStations.length;

        // return
        return new Pair<>(ret, rec);
    }

    private static void buildPath(int cstate, ArrayList<PathList> rec, PathList path, int dep, int m, Edge.CostT[][] f, Edge.CostT[][] msp, int[] midStations, int s) {
        int u = cstate >> m;
        int mask = cstate & ((1 << m) - 1);
        path.add(midStations[u]);

        if(dep == 1) { // the start point
            path.add(s);
            rec.add(path);
            return;
        }

        int tmask = mask ^ (1 << u);
        for(int i = 0;i < m;i ++) {
            if(f[i][tmask] == null)
                continue;
            if(f[i][tmask].add(msp[i][u]).compareTo(f[u][mask]) == 0) {
                PathList cpath = path.copy();
                buildPath((i << m) | tmask, rec, cpath, dep - 1, m, f, msp, midStations, s);
            }
        }
    }
}
