package core.dc;

import excs.DCException;
import javafx.util.Pair;

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
public class BlockDC extends DC {
    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    private int blockSize;

    public BlockDC(DCAlgorithm dc, CatchAlgorithm ca, int blockSize) {
        super(dc, ca);
        setBlockSize(blockSize);
    }

    @Override
    public void compress(InputStream in, OutputStream out)
    throws DCException {
        try {
            dumpDCCA(out);

            int bufferLength = 0;
            byte[] buffer = new byte[blockSize];

            while(true) {
                // read in bytes
                int readLength = in.read(buffer, bufferLength, blockSize - bufferLength);
                if(readLength == -1 && bufferLength == 0)
                    break;
                if(readLength != -1)
                    bufferLength += readLength;

                // use catch algorithm to generate token sequence
                Pair<Token[], Integer> p = catchAlg.parse(buffer, 0, bufferLength);
                Token[] tokens = p.getKey();
                int remainOffset = p.getValue();

                // compress
                byte[] compressedData = dcAlg.compress(tokens, catchAlg);

                // write bytes length
                int compressedDataLength = compressedData.length;
                for(int i = 0;i < 4;i ++) {
                    out.write((byte)(compressedDataLength & 0xff));
                    compressedDataLength >>= 8;
                }

                // write bytes
                out.write(compressedData);

                // calculate remain bytes
                bufferLength = bufferLength - remainOffset;
                System.arraycopy(buffer, remainOffset, buffer, 0, bufferLength);
            }
        } catch (IOException ioe) {
            throw new DCException(ioe);
        }
    }

    @Override
    public void decompress(InputStream in, OutputStream out)
    throws DCException {
        try {
            // load dc ca info
            loadDCCA(in);

            byte[] buffer = new byte[blockSize];

            while(true) {
                // read in buffer length
                int bufferLength = 0;
                boolean eofFlag = false;
                for(int i = 0;i < 4;i ++) {
                    int cb = in.read();
                    if(cb == -1) {
                        if(i == 0) {
                            // first byte cannot read, break
                            eofFlag = true;
                            break;
                        } else {
                            throw new DCException(
                                "Wrong file syntax: cannot load buffer length of current block");
                        }
                    }

                    bufferLength |= (cb << (8 * i));
                }

                if(eofFlag)
                    break;

                // read in buffer
                int readLength = in.read(buffer, 0, bufferLength);

                if(readLength != bufferLength)
                    throw new DCException("Wrong file syntax: buffer length illegal");

                // decompress
                Token[] tokens = dcAlg.decompress(buffer, catchAlg);

                // format tokens into bytes
                for(Token t: tokens)
                    out.write(t.dump());
            }
        } catch (IOException ioe) {
            throw new DCException(ioe);
        }
    }
}
