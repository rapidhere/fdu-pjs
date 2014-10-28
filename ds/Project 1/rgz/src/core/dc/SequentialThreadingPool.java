package core.dc;

import java.util.PriorityQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * a threading pool that can manage job to do sequential works
 *
 * WARNING: this module is not fully tested and have serious problems
 */

public class SequentialThreadingPool {
    /**
     * The thread to run task
     */
    private class TaskThread extends Thread {
        private boolean stopFlag = false;

        @Override
        public void run() {
            // loop over, waiting for task
            while(true) {
                ThreadingSequentialTask task;
                try {
                    // try to poll a task
                    task = queue.poll(1000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    continue;
                }

                if(task == null) { // if no task
                    if(stopFlag  && queue.isEmpty()) // if ended
                        break;
                    continue;
                }

                // do concurrent task
                task.run();

                // add sequential task into sequential thread
                sequentialThread.add(task);
            }
        }

        /**
         * stop the thread
         */
        public void stopRunning() {
            stopFlag = true;
        }
    }

    /**
     *  the task thread to run sequential task
     */
    private class SequentialTaskThread extends Thread{
        private boolean stopFlag = false;
        PriorityQueue<ThreadingSequentialTask> heap = new PriorityQueue<>();
        Lock lock = new ReentrantLock();
        Condition addCondition = lock.newCondition();

        @Override
        public void run() {
            try {
                while(true) {
                    try {
                        // lock up
                        lock.lock();
                        // waiting for task ID to become minID
                        while(heap.isEmpty() || heap.peek().taskId != minID) {
                            addCondition.await(1000, TimeUnit.MILLISECONDS);
                            if(stopFlag && heap.isEmpty())
                                break;
                        }

                        // ended
                        if(stopFlag && heap.isEmpty())
                            break;

                        // get task
                        ThreadingSequentialTask task = heap.poll();

                        // run
                        task.sequentialTaskRun();

                        // inc minID
                        minID ++;
                    } finally {
                        // unlock the working lock
                        lock.unlock();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /**
         * stop current thread
         */
        public void stopRunning() {
            stopFlag = true;
        }

        /**
         * add a sequential task
         * @param task the sequential task
         */
        public void add(ThreadingSequentialTask task) {
            lock.lock();
            try {
                heap.add(task);
                addCondition.signal();
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * the threading task to put into sequential threading pool
     */
    public static abstract class ThreadingSequentialTask implements Comparable<ThreadingSequentialTask> {
        private int taskId;

        // job do concurrently
        public abstract void run();

        // job do sequentially
        public abstract void sequentialTaskRun();

        @Override
        final public int compareTo(ThreadingSequentialTask o) {
            return taskId - o.taskId;
        }
    }

    private BlockingQueue<ThreadingSequentialTask> queue;
    private TaskThread threads[];
    private SequentialTaskThread sequentialThread = new SequentialTaskThread();

    private int nID;
    private int minID;

    /**
     * create a sequential threading pool with fixed size
     * @param size the size of pool
     */
    public SequentialThreadingPool(int size) {
        nID = 0;
        minID = 0;
        queue = new ArrayBlockingQueue<>(size);
        threads = new TaskThread[size];
        for(int i = 0;i < size;i ++) {
            TaskThread t = new TaskThread();
            threads[i] = t;
            t.start();
        }

        sequentialThread.start();
    }

    /**
     * add a task into threading pool
     * @param task the task to add
     * @throws InterruptedException
     */
    public void addTask(ThreadingSequentialTask task) throws InterruptedException {
        task.taskId = nID ++;
        queue.put(task);
    }

    /**
     * join all the threading pools, waiting for remain works to end
     */
    public void joinAll() {
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
