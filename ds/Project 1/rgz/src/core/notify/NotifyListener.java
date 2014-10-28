package core.notify;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * The listener of notify message
 */

public interface NotifyListener <T extends NotifyMessage> {
    void notify(T msg);
}
