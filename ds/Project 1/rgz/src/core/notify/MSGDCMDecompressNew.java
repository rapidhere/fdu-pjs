package core.notify;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * MSG: DCM start to decompress a new job
 */
public class MSGDCMDecompressNew extends NotifyMessage {
    String path;
    int nFile, totFile;

    /**
     * create a new Decompress new message
     * @param path the path of file
     * @param nFile file index
     * @param totFile total files
     */
    public MSGDCMDecompressNew(String path, int nFile, int totFile) {
        this.path = path;
        this.nFile = nFile;
        this.totFile = totFile;
    }

    /**
     * get the path of current file
     * @return the string of path
     */
    public String getPath() {return path;}

    /**
     * get the file index
     * @return file index
     */
    public int getNFile() {return nFile;}

    /**
     * get total files
     * @return total files
     */
    public int getTotFile() {return totFile;}
}
