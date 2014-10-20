package core.notify;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
abstract public class NotifyMessage {
    public static final int
        NMSG_BLOCK_DCM_START_NEW = 1,
        NMSG_TAR_BUILDING_INDEX = 2,
        NMSG_DCM_COMPRESS_NEW = 3,
        NMSG_TAR_DUMPING_INDEX = 4,
        NMSG_TAR_LOADING_INDEX = 5,
        NMSG_DCM_DECOMPRESS_NEW = 6,
        NMSG_DCM_DECOMPRESS_NEW_FILE = 7;

    protected int msgId;

    public NotifyMessage(int msgId) {
        this.msgId = msgId;
    }

    public int getMessageId() {
        return msgId;
    }
}
