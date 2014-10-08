package core.dc;

import excs.DCUpdateException;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */

public interface DCCompressAlgorithm {
    /**
     * update several bytes to the dcer.
     * this function will not actually do the dc.
     * @param bytes the bytes to update
     * @return number of bytes read
     */
    void update(byte[] bytes) throws DCUpdateException;

    /**
     * compress the data, and cleanup the updated data
     * after call this function, all inner data will clean up
     * @return compressed data
     */
    byte[] compress();
}