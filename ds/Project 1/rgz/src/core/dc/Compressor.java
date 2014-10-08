package core.dc;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */

abstract public class Compressor {
    protected DCAlgorithm dcAlg;

    protected Compressor() {}
    protected Compressor(DCAlgorithm dc) {
        dcAlg = dc;
    }

    abstract public int update(byte[] bytes);
    abstract public byte[] dump();
}
