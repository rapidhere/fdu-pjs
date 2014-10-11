package core.dc;

import excs.*;
import ui.Config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */

abstract public class DC {
    protected DCAlgorithm dcAlg;
    protected CatchAlgorithm catchAlg;

    protected DC(DCAlgorithm dc, CatchAlgorithm ca) {
        dcAlg = dc;
        catchAlg = ca;
    }

    abstract public void compress(InputStream in, OutputStream out) throws DCException;
    abstract public void decompress(InputStream in, OutputStream out) throws DCException;

    protected void dumpDCCA(OutputStream out)
    throws DCException {
        try {
            // write catch algorithm id
            try {
                out.write(Config.getCatchAlgorithmId(catchAlg));
            } catch (UnknownCatchAlgorithm e) {
                throw new DCException(e.getMessage());
            }

            // write dc algorithm id
            try {
                out.write(Config.getDCAlgorithmId(dcAlg));
            } catch (UnknownDCAlgorithm e) {
                throw new DCException(e.getMessage());
            }
        } catch (IOException ioe) {
            throw new DCException(ioe);
        }
    }

    protected void loadDCCA(InputStream in)
    throws DCException {
        try {
            byte b[] = new byte[2];
            if(in.read(b) != 2)
                throw new DCException("Wrong file syntax: cannot load dc ca info!");

            // read dc algorithm
            try {
                dcAlg = Config.getDCAlgorithmById(b[1]);
            } catch (UnknownDCAlgorithmId e) {
                throw new DCException(e.getMessage());
            }

            // read catch algorithm
            try {
                catchAlg = Config.getCatchAlgorithmById(b[0]);
            } catch (UnknownCatchAlgorithmId e) {
                throw new DCException(e.getMessage());
            }
        } catch (IOException ioe) {
            throw new DCException(ioe);
        }
    }
}
