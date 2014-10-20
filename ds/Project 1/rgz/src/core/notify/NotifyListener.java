package core.notify;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */

public interface NotifyListener <T extends NotifyMessage> {
    void notify(T msg);
}
