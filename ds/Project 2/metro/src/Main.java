import alg.Floyd;
import alg.ShortestPathWithMustPassVertex;
import graph.Edge;
import graph.MetroGraph;

import java.io.*;

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
        // get reader
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                new InputStreamReader(
                    new FileInputStream("data/metrolines.txt"), "UTF8"));
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
            String line;
            int curLineNumber;
            boolean isNumber;
            while((line = reader.readLine()) != null) {
                // ignore empty line
                line = line.trim();
                if(line.length() == 0)
                    continue;

                // this
                isNumber = true;
                try {
                    curLineNumber = Integer.valueOf(line);
                } catch (NumberFormatException e) {
                     isNumber = false;
                }

                if(isNumber)
                    continue;
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static String[] readRequire() {
        return null;
    }
}
