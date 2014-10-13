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
public class BlockDCM extends DCM {
    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    private int blockSize;

    public BlockDCM(DCAlgorithm dc, CatchAlgorithm ca, int blockSize) {
        super(dc, ca);
        setBlockSize(blockSize);
    }

    public BlockDCM() {
        super(null, null);
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
                if(readLength == -1) {
                    // write remain buffer
                    int minusBufferLength = - bufferLength;
                    for(int i = 0;i < 4;i ++) {
                        out.write((byte)(minusBufferLength & 0xff));
                        minusBufferLength >>= 8;
                    }
                    out.write(buffer, 0, bufferLength);
                    break;
                }

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

            while(true) {
                // read in buffer length
                int bufferLength = 0;
                for(int i = 0;i < 4;i ++) {
                    int cb = in.read();
                    if(cb == -1) {
                        throw new DCException(
                            "Wrong file syntax: cannot load buffer length of current block");
                    }

                    bufferLength |= ((cb & 0xff) << (8 * i));
                }

                if(bufferLength <= 0) {
                    byte[] bytes = new byte[-bufferLength];
                    int readBytes = in.read(bytes, 0, -bufferLength);
                    if(readBytes != -bufferLength || in.read() != -1)
                        throw new DCException(
                            "Wrong file syntax: cannot load last uncompressed buffer of this rgz");
                    out.write(bytes, 0, -bufferLength);
                    break;
                }

                // read in buffer
                byte[] buffer = new byte[bufferLength];
                int readLength = in.read(buffer, 0, bufferLength);

                if(readLength != bufferLength)
                    throw new DCException("Wrong file syntax: buffer length illegal");

                // decompress
                Token[] tokens = dcAlg.decompress(buffer, 0, bufferLength, catchAlg);

                // format tokens into bytes
                out.write(catchAlg.merge(tokens));
            }
        } catch (IOException ioe) {
            throw new DCException(ioe);
        }
    }
}
