import alg.AllPairShortestPath;
import alg.ShortestPathWithMustPassVertex;
import alg.Utils;
import graph.Edge;
import graph.MetroGraph;
import graph.PathList;
import graph.Vertex;
import javafx.util.Pair;

import java.io.*;
import java.util.*;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 */
public class Main {
    public static void main(String args[]) {
        // pre-work
        MetroGraph G = readIn();
        Edge.CostT[][] rawDist = AllPairShortestPath.runDijkstra(G);

        // deal with requires
        Scanner input = null;
        try {
            FileInputStream fin = new FileInputStream("data/testcases.txt");
            // ignore header
            //noinspection ResultOfMethodCallIgnored
            fin.read();
            //noinspection ResultOfMethodCallIgnored
            fin.read();
            //noinspection ResultOfMethodCallIgnored
            fin.read();

            // create scanner
            input = new Scanner(fin, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        while(input.hasNextLine()) {
            String[] require = input.nextLine().split(" ");
            int[] midStationIndex = new int[require.length - 2];
            for(int i = 1;i < require.length - 1;i ++) {
                midStationIndex[i - 1] = G.getStationIndex(require[i]);
            }

            Pair<Edge.CostT, ArrayList<PathList>> ret =
            ShortestPathWithMustPassVertex.runShortestPathWithMustPassVertex(
                    G, rawDist,
                    midStationIndex,
                    G.getStationIndex(require[0]),
                    G.getStationIndex(require[require.length - 1])
            );

            Edge.CostT cost = ret.getKey();
            ArrayList<PathList> paths = ret.getValue();

            System.out.println("Time: " + cost.time + ", Exchange: " + cost.change);

            ArrayList<PathList> retPaths = new ArrayList<>();
            ArrayList<PathList> midPaths = new ArrayList<>();
            for(PathList p: paths) {
                int csize = retPaths.size();
                getPath(p, p.size() - 1, rawDist, G, retPaths, new PathList());
                int dsize = retPaths.size();
                for(int i = 0;i < dsize - csize;i ++)
                    midPaths.add(p);
            }

            Set<Long> vis = new HashSet<>();
            for (int ci = 0;ci < retPaths.size();ci ++) {
                PathList cpath = retPaths.get(ci);
                PathList cmid = midPaths.get(ci);
                int cmidIndex = cmid.size() - 2;

                long hret = 0;
                for (int aCpath : cpath) {
                    hret *= 100007;
                    int li = G.getG()[aCpath].getLineIndex();
                    String sn = G.getG()[aCpath].getStationName();
                    if (li >= 18)
                        li -= 8;
                    hret += Utils.hashString(sn + li);
                    hret %= 1000000007;
                }

                for(int aMid : cmid) {
                    hret *= 100007;
                    hret += aMid;
                    hret %= 1000000007;
                }

                if (vis.contains(hret))
                    continue;
                vis.add(hret);

                int ptr = 0;
                boolean isMidStation = false;
                while (ptr < cpath.size() - 1) {
                    if(isMidStation) {
                        System.out.print("[" + G.getG()[cpath.get(ptr)].getStationName() + "]-");
                    } else {
                        System.out.print(G.getG()[cpath.get(ptr)].getStationName() + "-");
                    }

                    int jptr = ptr + 1;
                    while (!(cmidIndex > 0 && G.getVertex(cpath.get(jptr)).getStationIndex() == cmid.get(cmidIndex))
                            && jptr < cpath.size() - 1
                            && G.getG()[cpath.get(ptr)].getLineIndex() == G.getG()[cpath.get(jptr)].getLineIndex())
                        jptr++;

                    isMidStation = false;
                    if(cmidIndex > 0 && G.getVertex(cpath.get(jptr)).getStationIndex() == cmid.get(cmidIndex)) {
                        cmidIndex --;
                        isMidStation = true;
                    }

                    int li = G.getG()[cpath.get(ptr)].getLineIndex();
                    if(li >= 18)
                        li -= 8;
                    System.out.print("Line " + li + "-");
                    ptr = jptr;
                }
                System.out.println(G.getG()[cpath.get(cpath.size() - 1)].getStationName());
                /*
                int i;
                for(i = 0;i < cpath.size() - 1;i ++)
                    System.out.print("[" + G.getVertex(cpath.get(i)).getLineIndex() + "]" + G.getVertex(cpath.get(i)).getStationName() + " --> ");

                System.out.println("[" + G.getVertex(cpath.get(i)).getLineIndex() + "]" + G.getVertex(cpath.get(i)).getStationName());*/
            }
        }
    }

    private static void getPath(PathList p, int i, Edge.CostT[][] rd, MetroGraph G, ArrayList<PathList> retPaths, PathList cpath) {
        int pu = p.get(i), pv = p.get(i - 1);
        int[][] sit = G.getStationIndexTable();

        // get min cost
        Edge.CostT minCost = Edge.INFINITY_COST;
        for(int ii = 0;ii < sit[pu].length;ii ++)
            for(int jj = 0;jj < sit[pv].length;jj ++)
                minCost = Utils.min(minCost, rd[sit[pu][ii]][sit[pv][jj]]);

        // build path
        ArrayList<PathList> floydPaths = new ArrayList<>();
        for(int ii = 0;ii < sit[pu].length;ii ++)
            for(int jj = 0;jj < sit[pv].length;jj ++) {
                // this is the shortest path
                if (rd[sit[pu][ii]][sit[pv][jj]].compareTo(minCost) == 0) {
                    getFloydPath(sit[pu][ii], sit[pv][jj], G, rd, floydPaths, new PathList());
                }
            }

        for(PathList pl: floydPaths){
            PathList npath = cpath.copy();
            for (int j = 0;j < pl.size() - 1;j ++) {
                npath.add(pl.get(j));
            }
            if(i == 1) {
                npath.add(pl.get(pl.size() - 1));
                retPaths.add(npath);
            } else {
                getPath(p, i - 1, rd, G, retPaths, npath);
            }
        }
    }

    private static void getFloydPath(int u, int v, MetroGraph G, Edge.CostT rd[][], ArrayList<PathList> floydPaths, PathList cpath) {
        if(u == v) {
            cpath.add(v);
            floydPaths.add(cpath);
            return ;
        }

        cpath.add(u);
        Vertex cv = G.getG()[u];
        for(Edge e: cv.getEdges()) {
            if(e.getCost().add(rd[e.getV()][v]).compareTo(rd[u][v]) == 0) {
                PathList npath = cpath.copy();
                getFloydPath(e.getV(), v, G, rd, floydPaths, npath);
            }
        }
    }

    private static MetroGraph readIn() {
        Scanner sc = null;
        try {
            FileInputStream fin = new FileInputStream("data/metrolines.txt");
            // ignore header
            //noinspection ResultOfMethodCallIgnored
            fin.read();
            //noinspection ResultOfMethodCallIgnored
            fin.read();
            //noinspection ResultOfMethodCallIgnored
            fin.read();
            // create scanner
            sc = new Scanner(fin, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // read in lines
        int line;
        int totStations = 0;
        String stationName;
        String timeString;
        Map<Integer, ArrayList<Integer>> times = new HashMap<>();
        Map<Integer, ArrayList<String>> lines = new HashMap<>();

        MetroGraph mg = new MetroGraph();
        while(sc.hasNextInt()) {
            line = sc.nextInt();
            lines.put(line, new ArrayList<>());
            times.put(line, new ArrayList<>());
            if(line == 10 || line == 11) {
                lines.put(line + 8, new ArrayList<>());
                times.put(line + 8, new ArrayList<>());
            }

            while(sc.hasNext() && ! sc.hasNextInt()) {
                stationName = sc.next().trim();
                timeString = sc.next();

                mg.putStation(stationName);
                if(line == 10 || line == 11) {
                    if(! timeString.equals("---")) {
                        lines.get(line).add(stationName);
                        totStations ++;
                        times.get(line).add(calcTime(timeString));
                    }
                    timeString = sc.next();
                    if(! timeString.equals("---")) {
                        lines.get(line + 8).add(stationName);
                        totStations ++;
                        times.get(line + 8).add(calcTime(timeString));
                    }
                } else {
                    lines.get(line).add(stationName);
                    totStations ++;
                    times.get(line).add(calcTime(timeString));
                }
            }
        }

        // construct graph
        Vertex vs[] = new Vertex[totStations];
        int iStation = 0;

        // add lines into graph
        for(int l: lines.keySet()) {
            int timeCost;
            ArrayList<String> station = lines.get(l);
            ArrayList<Integer> time = times.get(l);

            for(int i = 0;i < station.size();i ++) {
                Vertex cv = new Vertex();
                cv.setIndex(iStation);
                cv.setLineIndex(l);
                cv.setStationName(station.get(i));
                cv.setStationIndex(mg.getStationIndex(cv.getStationName()));

                vs[iStation] = cv;
                if(i != 0) {
                    timeCost = time.get(i) - time.get(i - 1);
                    cv.addEdge(iStation - 1, new Edge.CostT(timeCost, 0));
                    vs[iStation - 1].addEdge(iStation, new Edge.CostT(timeCost, 0));
                }
                iStation ++;
            }

            // line 4 is a loop
            if(l == 4) {
                vs[iStation - 1].addEdge(iStation - station.size(), new Edge.CostT(2, 0));
                vs[iStation - station.size()].addEdge(iStation - 1, new Edge.CostT(2, 0));
            }
        }

        // add exchange stations
        for(int si: mg.getStationIndexes()) {
            ArrayList<Integer> sameStations = new ArrayList<>();
            for(int i = 0;i < totStations;i ++)
                if(vs[i].getStationIndex() == si)
                    sameStations.add(i);

            for(int i = 0;i < sameStations.size();i ++) {
                if (sameStations.size() != 1)
                    vs[sameStations.get(i)].setExchange(true);

                for (int j = 0; j < sameStations.size(); j++) {
                    if (i == j) continue;
                    vs[sameStations.get(i)].addEdge(sameStations.get(j), new Edge.CostT(0, 1));
                }
            }
        }

        // set the vertex array
        mg.setG(vs);

        // other job
        mg.buildStationIndexTable();

        return mg;
    }

    private static int calcTime(String timeString) {
        String[] t = timeString.trim().split(":");
        return Integer.valueOf(t[0]) * 60 + Integer.valueOf(t[1]);
    }
}