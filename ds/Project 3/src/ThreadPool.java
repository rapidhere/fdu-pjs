import java.io.PrintStream;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */


public class ThreadPool {
    private final static int poolSize = 4;
    private Lock outputLock =  new ReentrantLock();
    BlockingQueue<Task> taskQueue = new ArrayBlockingQueue<Task>(20);
    WorkingThread[] threadPool = new WorkingThread[poolSize];

    public class WorkingThread extends Thread {
        private int tid;
        private boolean exitFlag = false;
        public WorkingThread(int tid) {
            this.tid = tid;
        }

        public void run() {
            while(true) {
                try {
                    Task t = taskQueue.poll(10, TimeUnit.MILLISECONDS);

                    if(t == null) {
                        if(exitFlag)
                            break;
                    } else {
                        _run(t.db, t.dbDesc, t.output, t.aa, t.query, t.queryDesc);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void exit() {
            exitFlag = true;
        }

        public void _run(ArrayList<String> db, ArrayList<String> dbDesc, PrintStream output, ACAutomaton aa, ArrayList<String> query, ArrayList<String> queryDesc) {
            ArrayList<ArrayList<Integer>> resultOffset = new ArrayList<>();
            ArrayList<ArrayList<Integer>> resultIndex = new ArrayList<>();

            for(int i = 0;i < query.size();i ++) {
                resultOffset.add(new ArrayList<Integer>());
                resultIndex.add(new ArrayList<Integer>());
            }

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

    static public class Task {
        public PrintStream output;
        private ArrayList<String> db = new ArrayList<>();
        private ArrayList<String> dbDesc = new ArrayList<>();

        private ACAutomaton aa;
        private ArrayList<String> query;
        private ArrayList<String> queryDesc;

        Task(ArrayList<String> db, ArrayList<String> dbDesc, PrintStream output, ACAutomaton aa, ArrayList<String> query, ArrayList<String> queryDesc) {
            this.db = db;
            this.dbDesc = dbDesc;
            this.output = output;

            this.aa = aa;
            this.query = query;
            this.queryDesc = queryDesc;
        }
    }

    public ThreadPool() {
        for(int i = 0;i < threadPool.length;i ++) {
            threadPool[i] = new WorkingThread(i);
        }
    }

    public void start() {
        for (WorkingThread aThreadPool : threadPool) {
            aThreadPool.start();
        }
    }

    public void end() {
        for(WorkingThread t: threadPool) {
            try {
                t.exit();
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void pushTask(Task t) {
        try {
            taskQueue.put(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}