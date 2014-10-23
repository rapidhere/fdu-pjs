package core.dc;

import java.util.PriorityQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SequentialThreadingPool {
    public class TaskThread extends Thread {
        private boolean stopFlag = false;
        private int currentTaskId = -1;
        private int workerId;

        @Override
        public void run() {
            while(true) {
                ThreadingSequentialTask task;
                try {
                    task = queue.poll(1000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    continue;
                }

                if(task == null) {
                    //System.err.println("Waiting + " + workerId);
                    if(stopFlag  && queue.isEmpty())
                        break;
                    continue;
                }

                currentTaskId = task.taskId;
                task.run();

                sequentialThread.add(task);
            }
        }

        public void stopRunning() {
            stopFlag = true;
        }
    }

    public class SequentialTaskThread extends Thread{
        private boolean stopFlag = false;
        PriorityQueue<ThreadingSequentialTask> heap = new PriorityQueue<>();
        Lock lock = new ReentrantLock();
        Condition addCondition = lock.newCondition();
        //Condition notFullCondition = lock.newCondition();
        //final int heapSize = size;

        @Override
        public void run() {
            try {
                while(true) {
                    try {
                        lock.lock();
                        while(heap.isEmpty() || heap.peek().taskId != minID) {
                            addCondition.await(1000, TimeUnit.MILLISECONDS);
                            if(stopFlag && heap.isEmpty())
                                break;
                        }

                        if(stopFlag && heap.isEmpty())
                            break;

                        ThreadingSequentialTask task = heap.poll();
                        //notFullCondition.signal();
                        task.sequentialTaskRun();
                        minID ++;
                    } finally {
                        lock.unlock();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void stopRunning() {
            stopFlag = true;
        }

        public void add(ThreadingSequentialTask task) {
            lock.lock();
            try {
                //while(heap.size() == heapSize)
                //    notFullCondition.await();

                heap.add(task);
                addCondition.signal();
            } finally {
                lock.unlock();
            }
        }
    }

    public static abstract class ThreadingSequentialTask implements Comparable<ThreadingSequentialTask> {
        private int taskId;
        public abstract void run();
        public abstract void sequentialTaskRun();

        @Override
        final public int compareTo(ThreadingSequentialTask o) {
            return taskId - o.taskId;
        }
    }

    private BlockingQueue<ThreadingSequentialTask> queue;
    private TaskThread threads[];
    private SequentialTaskThread sequentialThread = new SequentialTaskThread();
    private int size;

    private int nID;
    private int minID;

    public SequentialThreadingPool(int size) {
        this.size = size;
        nID = 0;
        minID = 0;
        queue = new ArrayBlockingQueue<>(size);
        threads = new TaskThread[this.size];
        for(int i = 0;i < size;i ++) {
            TaskThread t = new TaskThread();
            threads[i] = t;
            t.workerId = i;
            t.start();
        }

        sequentialThread.start();
    }

    public void addTask(ThreadingSequentialTask task) throws InterruptedException {
        task.taskId = nID ++;
        queue.put(task);
    }

    public void joinAll() {
        //System.err.println("joining ...");
        for(TaskThread t: threads) {
            t.stopRunning();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        sequentialThread.stopRunning();
        try {
            sequentialThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
