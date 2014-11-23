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
 *
 * Main Entry of app
 */

public class Main {
    public static void main(String args[]) {
        // pre-work
        MetroGraph G = readIn();
        Edge.CostT[][] rawDist = AllPairShortestPath.runDijkstra(G);

        // deal with requires
        Scanner input = new Scanner(System.in, "UTF-8");

        // read in requires
        while(input.hasNextLine()) {
            String[] require = input.nextLine().split(" ");
            int[] midStationIndex = new int[require.length - 2];

            // convert into mid stations
            for(int i = 1;i < require.length - 1;i ++) {
                midStationIndex[i - 1] = G.getStationIndex(require[i]);
            }

            // run
            Pair<Edge.CostT, ArrayList<PathList>> ret =
            ShortestPathWithMustPassVertex.runShortestPathWithMustPassVertex(
                    G, rawDist,
                    midStationIndex,
                    G.getStationIndex(require[0]),
                    G.getStationIndex(require[require.length - 1])
            );

            // get cost and paths
            Edge.CostT cost = ret.getKey();
            ArrayList<PathList> paths = ret.getValue();

            // print cost
            System.out.println("Time: " + cost.time + ", Exchange: " + cost.change);

            // ready to construct paths
            ArrayList<PathList> retPaths = new ArrayList<>();
            ArrayList<PathList> midPaths = new ArrayList<>();

            // for each mid station path, construct the path
            for(PathList p: paths) {
                int csize = retPaths.size();
                getPath(p, p.size() - 1, rawDist, G, retPaths, new PathList());
                int dsize = retPaths.size();

                // add mid paths of current path
                for(int i = 0;i < dsize - csize;i ++)
                    midPaths.add(p);
            }

            // print paths
            for (int ci = 0;ci < retPaths.size();ci ++) {
                PathList cpath = retPaths.get(ci);
                PathList cmid = midPaths.get(ci);

                int cmidIndex = cmid.size() - 2; // pointer to current mid station
                int ptr = 0; // pointer to current path
                boolean isMidStation = false;

                // start print
                while (ptr < cpath.size() - 1) {
                    if(isMidStation) { // print mid station
                        System.out.print("[" + G.getG()[cpath.get(ptr)].getStationName() + "]-");
                    } else {
                        System.out.print(G.getG()[cpath.get(ptr)].getStationName() + "-");
                    }

                    // next pos of ptr
                    int jptr = ptr + 1;
                    while (!(cmidIndex > 0 && G.getVertex(cpath.get(jptr)).getStationIndex() == cmid.get(cmidIndex))
                            && jptr < cpath.size() - 1
                            && G.getG()[cpath.get(ptr)].getLineIndex() == G.getG()[cpath.get(jptr)].getLineIndex())
                        jptr++;

                    // reset is midStation
                    isMidStation = false;
                    if(cmidIndex > 0 && G.getVertex(cpath.get(jptr)).getStationIndex() == cmid.get(cmidIndex)) {
                        cmidIndex --;
                        isMidStation = true;
                    }

                    // get line index
                    int li = G.getG()[cpath.get(ptr)].getLineIndex();
                    if(li >= 18)
                        li -= 8;

                    // print line
                    System.out.print("Line " + li + "-");
                    ptr = jptr;
                }

                // print last station
                System.out.println(G.getG()[cpath.get(cpath.size() - 1)].getStationName());
            }
        }
    }

    // get the path with specified mid stations
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

        // create vis hash set used to check the line is visited
        Set<Long> vis = new HashSet<>();

        // for each floydPath
        for(PathList pl: floydPaths){
            // get a copy of path
            PathList npath = cpath.copy();
            for (int j = 0;j < pl.size() - 1;j ++) {
                npath.add(pl.get(j));
            }

            // calc hash
            long hret = 0;
            for (int aCpath : npath) {
                hret *= 100007;
                int li = G.getG()[aCpath].getLineIndex();
                String sn = G.getG()[aCpath].getStationName();
                if (li >= 18)
                    li -= 8;
                hret += Utils.hashString(sn + li);
                hret %= 1000000007;
            }

            // check if exist
            if (vis.contains(hret))
                continue;
            vis.add(hret);

            // add path and do next
            if(i == 1) {
                npath.add(pl.get(pl.size() - 1));
                retPaths.add(npath);
            } else {
                getPath(p, i - 1, rd, G, retPaths, npath);
            }
        }
    }


    // get floyd path between u and v
    private static void getFloydPath(int u, int v, MetroGraph G, Edge.CostT rd[][], ArrayList<PathList> floydPaths, PathList cpath) {
        if(u == v) { // end of construct add it
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
        // read in next line number
        while(sc.hasNextInt()) {
            // get line number
            line = sc.nextInt();

            // create new array list
            lines.put(line, new ArrayList<>());
            times.put(line, new ArrayList<>());
            if(line == 10 || line == 11) {
                lines.put(line + 8, new ArrayList<>());
                times.put(line + 8, new ArrayList<>());
            }

            // read in stations
            while(sc.hasNext() && ! sc.hasNextInt()) {
                // get station name and time
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


    // convert time into seconds
    private static int calcTime(String timeString) {
        String[] t = timeString.trim().split(":");
        return Integer.valueOf(t[0]) * 60 + Integer.valueOf(t[1]);
    }
}