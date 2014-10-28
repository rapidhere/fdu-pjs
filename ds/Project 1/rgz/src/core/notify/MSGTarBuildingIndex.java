package core.notify;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * MSG: Tar is building index
 */
public class MSGTarBuildingIndex extends NotifyMessage {
    private String path;

    /**
     * create a new Tar building index message
     * @param path the path building
     */
    public MSGTarBuildingIndex(String path) {
        this.path = path;
    }

    /**
     * get the index building path
     * @return string of path
     */
    public String getPath() {
        return path;
    }
}
