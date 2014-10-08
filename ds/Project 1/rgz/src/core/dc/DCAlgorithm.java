package core.dc;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */

public interface DCAlgorithm {
    /**
     * update several bytes to the dcer.
     * this function will not actually do the dc.
     * @param bytes the bytes to update
     * @return number of bytes read
     */
    int update(byte[] bytes);

    /**
     * compress the data, and cleanup the updated data
     * after call this function, all inner data will clean up
     * @return compressed data
     */
    byte[] compress();

    /**
     * decompress the data, and cleanup the updated data
     * must call importDC() first to import Decompress info
     * after call this function, all inner data will clean up
     * @return decompressed data
     */
    byte[] decompress();

    /**
     * dump the dc info into bytes that can be import with importDC
     * @return dumped dc info
     */
    byte[] dumpDC();

    /**
     * import dc info from the bytes dump by dumpDC
     * @param bytes bytes to import
     */
    void importDC(byte[] bytes);
}