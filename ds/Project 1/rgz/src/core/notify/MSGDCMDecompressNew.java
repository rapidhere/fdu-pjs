package core.notify;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class MSGDCMDecompressNew extends NotifyMessage {
    String path;
    int nFile, totFile;

    public MSGDCMDecompressNew(String path, int nFile, int totFile) {
        super(NotifyMessage.NMSG_DCM_DECOMPRESS_NEW);
        this.path = path;
        this.nFile = nFile;
        this.totFile = totFile;
    }

    public String getPath() {return path;}
    public int getNFile() {return nFile;}
    public int getTotFile() {return totFile;}
}
