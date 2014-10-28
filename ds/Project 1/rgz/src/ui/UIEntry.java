package ui;

import core.notify.NotifyThread;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Entry of the ui
 */
abstract public class UIEntry {
    abstract protected void run();

    final public void start() {
        // reset works
        // create notifier thread
        Thread notifierThread = NotifyThread.getNotifyThread();
        notifierThread.start();

        // run
        run();
    }
}
