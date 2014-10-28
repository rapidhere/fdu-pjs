package core.notify;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * MSG: DCM start to decompress a new file
 */
public class MSGDCMDecompressNewFile extends NotifyMessage {
    String path;

    /**
     * create a new DCM decompress file message
     * @param path the path of file
     */
    public MSGDCMDecompressNewFile(String path) {
        this.path = path;
    }

    /**
     * get the path of current decompressing file
     * @return the string of path
     */
    public String getPath() {return path;}
}
