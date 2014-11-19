import alg.Floyd;
import alg.ShortestPathWithMustPassVertex;
import graph.Edge;
import graph.MetroGraph;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 */
public class Main {
    public static void main(String args[]) {
        // pre-work
        MetroGraph G = readIn();
        Edge.CostT[][] rawDist = Floyd.runFloyd(G);

        // deal with requires
        String[] require;
        while((require = readRequire()) != null) {
            int[] midStationIndex = new int[require.length - 2];
            for(int i = 1;i < require.length - 1;i ++)
                midStationIndex[i] = G.getStationIndex(require[i]);

            ShortestPathWithMustPassVertex.runShortestPathWithMustPassVertex(
                G, rawDist,
                midStationIndex,
                G.getStationIndex(require[0]),
                G.getStationIndex(require[require.length - 1])
            );
        }
    }

    private static MetroGraph readIn() {
        return null;
    }

    private static String[] readRequire() {
        return null;
    }
}
