package core.dc;

import excs.DCUpdateException;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */

public interface DCDecompressAlgorithm {
    /**
     * update several bytes to the dcer.
     * this function will not actually do the dc.
     * @param bytes the bytes to update
     * @return number of bytes read
     */
    void update(byte[] bytes) throws DCUpdateException;

    /**
     * decompress the data, and cleanup the updated data
     * must call importDC() first to import Decompress info
     * after call this function, all inner data will clean up
     * @return decompressed data
     */
    byte[] decompress();
}