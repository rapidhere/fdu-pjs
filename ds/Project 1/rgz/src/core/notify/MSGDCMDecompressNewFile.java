package core.notify;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class MSGDCMDecompressNewFile extends NotifyMessage {
    String path;

    public MSGDCMDecompressNewFile(String path) {
        super(NotifyMessage.NMSG_DCM_DECOMPRESS_NEW_FILE);
        this.path = path;
    }

    public String getPath() {return path;}
}
