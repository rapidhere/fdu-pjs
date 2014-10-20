package core.notify;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class MSGDCMCompressNew extends NotifyMessage {
    private String path;
    private int nFile, totFile;

    public MSGDCMCompressNew(String path, int nFile, int totFile) {
        super(NotifyMessage.NMSG_DCM_COMPRESS_NEW);
        this.nFile = nFile;
        this.totFile = totFile;
        this.path = path;
    }

    public String getPath() {
        return path;
    }
    public int getNFile() { return  nFile;}
    public int getTotFile() {return totFile;}
}
