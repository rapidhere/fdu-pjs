package core.dc;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
abstract public interface DCAlgorithm {
    abstract public byte[] compress(Token[] tokens);
    abstract public byte[] decompress(byte[] bytes);
}
