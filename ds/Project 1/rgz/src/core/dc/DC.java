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

            // if is fixed bytes cather, write byte info
            if(catchAlg instanceof CatchFixedBitsAlgorithm)
                out.write(((CatchFixedBitsAlgorithm) catchAlg).getBitLength());
            else if(catchAlg instanceof CatchFixedBytesAlgorithm)
                out.write(((CatchFixedBytesAlgorithm) catchAlg).getByteLength());

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
            int b;
            // read catch algorithm
            try {
                b = in.read();
                if(b == -1)
                    throw new DCException("Wrong file syntax: cannot load dc ca info!");

                catchAlg = Config.getCatchAlgorithmById((byte)b);
                if(catchAlg instanceof CatchFixedBitsAlgorithm || catchAlg instanceof CatchFixedBytesAlgorithm) {
                    b = in.read();
                    if(b == -1)
                        throw new DCException("Wrong file syntax: cannot load dc ca info!");
                    if(catchAlg instanceof CatchFixedBitsAlgorithm)
                        ((CatchFixedBitsAlgorithm) catchAlg).setBitLength((byte) b);
                    else
                        ((CatchFixedBytesAlgorithm) catchAlg).setByteLength((byte) b);
                }
            } catch (UnknownCatchAlgorithmId e) {
                throw new DCException(e.getMessage());
            }

            // read dc algorithm
            try {
                b = in.read();
                if(b == -1)
                    throw new DCException("Wrong file syntax: cannot load dc ca info!");

                dcAlg = Config.getDCAlgorithmById((byte)b);
            } catch (UnknownDCAlgorithmId e) {
                throw new DCException(e.getMessage());
            }


        } catch (IOException ioe) {
            throw new DCException(ioe);
        }
    }
}
