package core.notify;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class MSGTarBuildingIndex extends NotifyMessage {
    private String path;


    public MSGTarBuildingIndex(String path) {
        super(NotifyMessage.NMSG_TAR_BUILDING_INDEX);
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
