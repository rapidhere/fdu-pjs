import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 */
public class DnaMatching {
    private Scanner dbInput, queryInput;
    private PrintStream output;

    private ArrayList<String> db = new ArrayList<>();
    private ArrayList<String> dbDesc = new ArrayList<>();

    private static final int blockSize = 1024 * 1024;

    public DnaMatching(String db, String query, String output) throws FileNotFoundException {
        dbInput = new Scanner(new BufferedInputStream(new FileInputStream(db)));
        queryInput = new Scanner(new BufferedInputStream(new FileInputStream(query)));
        this.output = new PrintStream(new BufferedOutputStream(new FileOutputStream(output)));
    }

    public void run() throws InterruptedException {
        ArrayList<String> queryDesc = new ArrayList<>();
        ArrayList<String> query = new ArrayList<>();
        ACAutomaton curAA = new ACAutomaton();

        ThreadPool tp = new ThreadPool();
        tp.start();

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

            if(exitFlag || curAA.getTrieSize() >= blockSize) {
                tp.pushTask(new ThreadPool.Task(db, dbDesc, output, curAA, query, queryDesc));

                cIndex = 0;
                curAA = new ACAutomaton();
                query = new ArrayList<>();
                queryDesc = new ArrayList<>();
            }

            if(exitFlag)
                break;
        }

        tp.end();
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
