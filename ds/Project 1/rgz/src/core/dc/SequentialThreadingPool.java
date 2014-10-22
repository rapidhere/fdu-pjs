package core.dc;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class SequentialThreadingPool {
    public class TaskThread extends Thread {
        private boolean stopFlag = false;
        private int currentTaskId = -1;
        private int workerId;

        @Override
        public void run() {
            while(true) {
                workingLock[workerId].lock();
                ThreadingSequentialTask task;
                try {
                    task = queue.poll(100, TimeUnit.MILLISECONDS);
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
                //System.err.println("Task " + workerId + " get job: " + currentTaskId);
                task.run();

                for(int i = 0;i < size;i ++)
                    if(threads[i].currentTaskId != -1 && threads[i].currentTaskId < currentTaskId) {
                        //System.err.println("Task " + workerId + " get lock " + i);
                        workingLock[i].lock();
                        workingLock[i].unlock();
                    }

                //System.err.println("Task " + workerId + " output");
                task.sequentialTaskRun();

                workingLock[workerId].unlock();
                System.err.println("Task " + workerId + " free write lock");
                currentTaskId = -1;
            }
        }

        public void stopRunning() {
            stopFlag = true;
        }
    }

    public static abstract class ThreadingSequentialTask {
        private int taskId;
        public abstract void run();
        public abstract void sequentialTaskRun();
    }

    private BlockingQueue<ThreadingSequentialTask> queue = new LinkedBlockingQueue<>();
    private TaskThread threads[];
    private ReentrantLock workingLock[];
    private int size;

    private int nID;

    public SequentialThreadingPool(int size) {
        this.size = size;
        workingLock = new ReentrantLock[size];
        nID = 0;
        threads = new TaskThread[this.size];
        for(int i = 0;i < size;i ++) {
            TaskThread t = new TaskThread();
            threads[i] = t;
            t.workerId = i;
            t.start();
            workingLock[i] = new ReentrantLock(false);
        }
    }

    public void addTask(ThreadingSequentialTask task) {
        task.taskId = nID ++;
        queue.offer(task);
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
    }
}
