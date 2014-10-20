package core.notify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class Notifier {
    private Notifier() {}
    private static Notifier theNotifier = null;
    private Map<Integer, ArrayList<NotifyListener>> notifyListeners =
        new HashMap<Integer, ArrayList<NotifyListener>>();

    private BlockingQueue<NotifyMessage> messageQueue = new LinkedBlockingQueue<NotifyMessage>();

    synchronized public static Notifier getNotifier() {
        if(theNotifier == null) {
            theNotifier = new Notifier();
        }
        return theNotifier;
    }

    public void addNotifyMessage(NotifyMessage msg) {
        messageQueue.offer(msg);
    }

    public NotifyMessage pollNotifyMessage() throws InterruptedException {
        return messageQueue.take();
    }

    synchronized public void notify(NotifyMessage msg) {
        ArrayList<NotifyListener> listeners = notifyListeners.get(msg.getMessageId());
        if(listeners == null)
            return;

        for(NotifyListener listener: listeners)
            listener.notify(msg);
    }

    synchronized public void register(int msgId, NotifyListener listener) {
        if(notifyListeners.get(msgId) == null) {
            notifyListeners.put(msgId, new ArrayList<NotifyListener>());
        }

        ArrayList<NotifyListener> listeners = notifyListeners.get(msgId);
        listeners.add(listener);
    }
}
