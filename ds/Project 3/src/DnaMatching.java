import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 */
public class DnaMatching {
    private Scanner dbInput, queryInput;
    private PrintStream output;

    private ArrayList<String> db = new ArrayList<>();
    private ArrayList<String> dbDesc = new ArrayList<>();

    private int workingThread = 0;
    private int maxThread = 4;
    private Thread threads[] = new Thread[maxThread];

    private Lock outputLock = new ReentrantLock();

    private class MatchingThread extends Thread {
        private ArrayList<String> query;
        private ArrayList<String> queryDesc;

        private ArrayList<ArrayList<Integer>> resultOffset = new ArrayList<>();
        private ArrayList<ArrayList<Integer>> resultIndex = new ArrayList<>();

        private ACAutomaton aa;

        public MatchingThread(ACAutomaton aa, ArrayList<String> query, ArrayList<String> queryDesc) {
            this.aa = aa;
            this.query = query;
            this.queryDesc = queryDesc;

            for(int i = 0;i < query.size();i ++) {
                resultIndex.add(new ArrayList<Integer>());
                resultOffset.add(new ArrayList<Integer>());
            }
        }

        @Override
        public void run() {
            aa.build();

            for (int i = 0;i < db.size();i ++) {
                for (ACAutomaton.SearchResult s : aa.search(db.get(i))) {
                    int idx = s.getPatternIndex();
                    resultOffset.get(idx).add(s.getOffset());
                    resultIndex.get(idx).add(i);
                }
            }

            // print
            outputLock.lock();
            try {
                for (int i = 0; i < query.size(); i++) {
                    output.println(queryDesc.get(i));
                    ArrayList<Integer> offsets = resultOffset.get(i);
                    ArrayList<Integer> indexes = resultIndex.get(i);

                    if (offsets.size() == 0) {
                        output.println("    NOT FOUND");
                    } else {
                        for (int j = 0; j < offsets.size(); j++) {
                            int idx = indexes.get(j);
                            int offset = offsets.get(j);
                            output.println("    [" + dbDesc.get(idx) + "] at offset " + (offset - query.get(i).length() + 1));
                        }
                    }
                    output.println();
                }
            } finally {
                outputLock.unlock();
            }
        }
    }

    public DnaMatching(String db, String query, String output) throws FileNotFoundException {
        dbInput = new Scanner(new BufferedInputStream(new FileInputStream(db)));
        queryInput = new Scanner(new BufferedInputStream(new FileInputStream(query)));
        this.output = new PrintStream(new BufferedOutputStream(new FileOutputStream(output)));
    }

    public void run() throws InterruptedException {
        ArrayList<String> queryDesc = new ArrayList<>();
        ArrayList<String> query = new ArrayList<>();
        ACAutomaton curAA = new ACAutomaton();

        // read in db
        while(true) {
            String desc = dbInput.nextLine().trim().substring(1);
            if(desc.compareTo("EOF") == 0) {
                break;
            }
            String seq = dbInput.nextLine().trim();
            db.add(seq);
            dbDesc.add(desc);
        }

        // read in queries
        int cIndex = 0;
        while(true) {
            boolean exitFlag = false;
            String desc = queryInput.nextLine().trim().substring(1);
            if(desc.compareTo("EOF") == 0) {
                exitFlag = true;
            } else {
                String seq = queryInput.nextLine().trim();

                query.add(seq);
                queryDesc.add(desc);
                curAA.insertPattern(seq, cIndex);
                cIndex ++;
            }

            if(exitFlag || curAA.getTrieSize() >= 65536) {
                threads[workingThread] = new MatchingThread(curAA, query, queryDesc);
                threads[workingThread].start();
                workingThread ++;

                cIndex = 0;
                curAA = new ACAutomaton();
                query = new ArrayList<>();
                queryDesc = new ArrayList<>();
            }

            if(workingThread == maxThread || exitFlag) {
                for(int i = 0;i < workingThread;i ++) {
                    threads[i].join();
                    threads[i] = null;
                }
                workingThread = 0;
            }

            if(exitFlag)
                break;
        }

        // flush output
        output.close();
    }

    public static void main(String args[]) throws Exception {
        long startTime = System.nanoTime();
        DnaMatching dm = new DnaMatching(args[0], args[1], args[2]);
        dm.run();
        long endTime = System.nanoTime();

        double timeCost = (endTime - startTime) / 1e6;

        System.err.println("Time cost: " + timeCost + " ms");
    }
}
