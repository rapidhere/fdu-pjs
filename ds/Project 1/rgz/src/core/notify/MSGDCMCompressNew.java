package core.notify;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * MSG: DCM start to compress a new file
 */
public class MSGDCMCompressNew extends NotifyMessage {
    private String path;
    private int nFile, totFile;

    /**
     * create a new Compress New message
     * @param path the path of file
     * @param nFile file index
     * @param totFile totalFiles
     */
    public MSGDCMCompressNew(String path, int nFile, int totFile) {
        this.nFile = nFile;
        this.totFile = totFile;
        this.path = path;
    }

    /**
     * get the path of current compressing file
     * @return the string of path
     */
    public String getPath() {
        return path;
    }

    /**
     * get the file index
     * @return the file index
     */
    public int getNFile() { return  nFile;}

    /**
     * get total files
     * @return total files
     */
    public int getTotFile() {return totFile;}
}
