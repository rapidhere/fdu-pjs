package core.notify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Notifier is used to distribute notify messages into listeners
 * SINGLETON
 */

public class Notifier {
    private Notifier() {}
    private static Notifier theNotifier = null;
    private Map<Class<? extends NotifyMessage>, ArrayList<? extends NotifyListener>> notifyListeners =
        new HashMap<>();

    private BlockingQueue<NotifyMessage> messageQueue = new LinkedBlockingQueue<>();

    /**
     * return the only notifier in the program
     * @return the singleton notifier
     */
    public static Notifier getNotifier() {
        if(theNotifier == null) {
            theNotifier = new Notifier();
        }
        return theNotifier;
    }

    /**
     * add a notify message into queue
     * @param msg notify message
     */
    public void addNotifyMessage(NotifyMessage msg) {
        messageQueue.offer(msg);
    }

    /**
     * poll a notify message from queue, blocking on none
     * @return a notify message
     * @throws InterruptedException
     */
    public NotifyMessage pollNotifyMessage() throws InterruptedException {
        return messageQueue.take();
    }

    private <T extends NotifyMessage> ArrayList<NotifyListener<T>>
    getListeners(Class<T> msgClass) {
        //noinspection unchecked cast
        return (ArrayList<NotifyListener<T>>) notifyListeners.get(msgClass);
    }

    /**
     * notify the message to listeners
     * @param msg the notify message
     * @param <T> the type of NotifyMessage
     */
    public <T extends NotifyMessage> void notify(T msg) {
        ArrayList<? extends NotifyListener> listeners = notifyListeners.get(msg.getClass());
        if(listeners == null)
            return;

        for(NotifyListener<T> listener: listeners)
            listener.notify(msg);
    }

    /**
     * register a listener
     * @param msgClass the class of message
     * @param listener the listener
     * @param <T> the type of message
     */
    public <T extends NotifyMessage> void
    register(Class<T> msgClass, NotifyListener<T> listener) {
        if(notifyListeners.get(msgClass) == null) {
            notifyListeners.put(msgClass, new ArrayList<>());
        }

        ArrayList<NotifyListener<T>> listeners = getListeners(msgClass);
        listeners.add(listener);
    }
}
