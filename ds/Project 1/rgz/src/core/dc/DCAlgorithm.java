package core.dc;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
abstract public interface DCAlgorithm {
    abstract public byte[] compress(CatchAlgorithm.Token[] tokens, CatchAlgorithm ca);
    abstract public CatchAlgorithm.Token[] decompress(byte[] bytes, int offset, int length, CatchAlgorithm ca);
}
