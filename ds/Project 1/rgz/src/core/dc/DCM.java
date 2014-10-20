package core.dc;

import excs.*;
import ui.Config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.ParameterizedType;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */

abstract public class DCM <T extends Token>{
    protected DCAlgorithm<T> dcAlg;
    protected CatchAlgorithm<T> catchAlg;
    protected Class<T> tokenClass;

    protected DCM(CatchAlgorithm<T> catchAlg, DCAlgorithm<T> dcAlg) {
        this.dcAlg = dcAlg;
        this.catchAlg = catchAlg;
        initTokenClass();
    }

    @SuppressWarnings("unchecked")
    private void initTokenClass() {
        this.tokenClass = (Class<T>)
            ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    abstract public void compress(InputStream in, OutputStream out) throws DCException;
    abstract public void decompress(InputStream in, OutputStream out) throws DCException;

    protected void dumpDCCA(OutputStream out)
    throws DCException {
        try {
            // write token id
            try {
                out.write(Config.getTokenId(tokenClass));
            } catch (UnknownToken e) {
                throw new DCException(e.getMessage());
            }
            // write dc algorithm id
            try {
                out.write(Config.getDCAlgorithmId(dcAlg));
            } catch (UnknownDCAlgorithm e) {
                throw new DCException(e.getMessage());
            }

            // dump catch algorithm
            catchAlg.dumpHeader(out);

        } catch (IOException ioe) {
            throw new DCException(ioe);
        }
    }

    public void setDC(DCAlgorithm<T> dc) {this.dcAlg = dc;}
    public void setCA(CatchAlgorithm<T> ca) {this.catchAlg = ca;}
}
