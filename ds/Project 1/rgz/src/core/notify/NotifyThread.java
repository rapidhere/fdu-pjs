package core.notify;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class NotifyThread extends Thread {
    public static NotifyThread notifyThread;

    public static NotifyThread getNotifyThread() {
        if(notifyThread == null) {
            notifyThread = new NotifyThread();
        }
        return notifyThread;
    }

    private NotifyThread() {}

    @Override
    public void run() {
        Notifier notifier = Notifier.getNotifier();

        //noinspection InfiniteLoopStatement
        while(true) {
            try {
                NotifyMessage msg = notifier.pollNotifyMessage();
                notifier.notify(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
