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
    private Map<Class<? extends NotifyMessage>, ArrayList<? extends NotifyListener>> notifyListeners =
        new HashMap<>();

    private BlockingQueue<NotifyMessage> messageQueue = new LinkedBlockingQueue<>();

    public static Notifier getNotifier() {
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

    private <T extends NotifyMessage> ArrayList<NotifyListener<T>>
    getListeners(Class<T> msgClass) {
        //noinspection unchecked cast
        return (ArrayList<NotifyListener<T>>) notifyListeners.get(msgClass);
    }

    public <T extends NotifyMessage> void notify(T msg) {
        ArrayList<? extends NotifyListener> listeners = notifyListeners.get(msg.getClass());
        if(listeners == null)
            return;

        for(NotifyListener<T> listener: listeners)
            listener.notify(msg);
    }

    public <T extends NotifyMessage> void
    register(Class<T> msgClass, NotifyListener<T> listener) {
        if(notifyListeners.get(msgClass) == null) {
            notifyListeners.put(msgClass, new ArrayList<>());
        }

        ArrayList<NotifyListener<T>> listeners = getListeners(msgClass);
        listeners.add(listener);
    }
}
