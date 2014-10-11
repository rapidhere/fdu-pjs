package core.dc;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
abstract public interface DCAlgorithm {
    abstract public byte[] compress(Token[] tokens, CatchAlgorithm ca);
    abstract public Token[] decompress(byte[] bytes, CatchAlgorithm ca);
}
