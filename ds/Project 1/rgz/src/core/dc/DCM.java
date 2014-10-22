package core.dc;

import excs.*;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */

abstract public class DCM<T extends Token> {
    protected DCAlgorithm<T> dcAlg;
    protected CatchAlgorithm<T> catchAlg;

    public void setDCAlgorithm(DCAlgorithm<T> dcAlg) {
        this.dcAlg = dcAlg;
    }

    public void setCatchAlgorithm(CatchAlgorithm<T> catchAlg) {
        this.catchAlg = catchAlg;
    }

    abstract public void compress(InputStream in, OutputStream out, SequentialThreadingPool tp) throws DCException;
    abstract public void decompress(InputStream in, OutputStream out, SequentialThreadingPool tp) throws DCException;
}
