package core.notify;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class MSGBlockDCMStartNew extends NotifyMessage {
    public MSGBlockDCMStartNew(int numberBlock, int totalBlock) {
        set(numberBlock, totalBlock);
    }

    private int nBlock, tBlock;
    public void set(int numberBlock, int totalBlock) {
        nBlock = numberBlock;
        tBlock = totalBlock;
    }

    public int getNumberBlock() {
        return nBlock;
    }

    public int getTotalBlock() {
        return tBlock;
    }
}
