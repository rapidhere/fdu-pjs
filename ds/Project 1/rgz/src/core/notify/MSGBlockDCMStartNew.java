package core.notify;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * MSG: block dcm start to compress a new block
 */
public class MSGBlockDCMStartNew extends NotifyMessage {
    public MSGBlockDCMStartNew(int numberBlock, int totalBlock) {
        set(numberBlock, totalBlock);
    }

    private int nBlock, tBlock;

    /**
     * set the current block and total blocks
     * @param numberBlock current block
     * @param totalBlock total blocks
     */
    public void set(int numberBlock, int totalBlock) {
        nBlock = numberBlock;
        tBlock = totalBlock;
    }

    /**
     * get current block index
     * @return the index
     */
    public int getNumberBlock() {
        return nBlock;
    }

    /**
     * get total blocks
     * @return total blocks
     */
    public int getTotalBlock() {
        return tBlock;
    }
}
