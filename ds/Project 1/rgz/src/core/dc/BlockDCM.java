package core.dc;

import core.notify.MSGBlockDCMStartNew;
import core.notify.Notifier;
import excs.DCException;
import javafx.util.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class BlockDCM <T extends Token> extends DCM<T> {
    public BlockDCM() {}

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    private int blockSize;

    @Override
    public void compress(InputStream in, OutputStream out, SequentialThreadingPool tp)
    throws DCException {
        // create threading poll
        try {
            int bufferLength = 0;
            byte[] buffer = new byte[blockSize];

            // calc block amount
            int totalBlock = (int) Math.ceil((double)in.available() / (double)blockSize) + 1;
            int blockIndex = 0;

            while(true) {
                // append message
                blockIndex ++;

                // read in bytes
                int readLength = in.read(buffer, bufferLength, blockSize - bufferLength);
                if(readLength == -1) {
                    tp.addTask(new CompressLastThreadingTask(buffer, bufferLength, out, blockIndex, totalBlock));
                    break;
                }

                bufferLength += readLength;

                // use catch algorithm to generate token sequence
                Pair<ArrayList<T>, Integer> p = catchAlg.parse(buffer, 0, bufferLength);
                ArrayList<T> tokens = p.getKey();
                int remainOffset = p.getValue();

                // compress
                CompressThreadingTask task = new CompressThreadingTask(tokens, out, blockIndex, totalBlock);
                tp.addTask(task);

                // calculate remain bytes
                bufferLength = bufferLength - remainOffset;
                System.arraycopy(buffer, remainOffset, buffer, 0, bufferLength);
            }
        } catch (IOException ioe) {
            throw new DCException(ioe);
        }
    }

    @Override
    public void decompress(InputStream in, OutputStream out, SequentialThreadingPool tp)
    throws DCException {
        try {
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
                    if(readBytes != -bufferLength)
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
                ArrayList<T> tokens = dcAlg.decompress(buffer, 0, bufferLength, catchAlg);

                // format tokens into bytes
                out.write(catchAlg.merge(tokens));
            }
        } catch (IOException ioe) {
            throw new DCException(ioe);
        }
    }


    class CompressThreadingTask extends SequentialThreadingPool.ThreadingSequentialTask {
        private ArrayList<T> tokens;
        private OutputStream out;
        private byte[] compressedData;
        private int blockIndex;
        private int totalBlock;

        public CompressThreadingTask(ArrayList<T> tokens, OutputStream out, int blockIndex, int totalIndex) {
            this.tokens = tokens;
            this.out = out;
            this.blockIndex = blockIndex;
            this.totalBlock = totalIndex;
        }

        @Override
        public void run() {
            Notifier.getNotifier().addNotifyMessage(new MSGBlockDCMStartNew(blockIndex, totalBlock));
            try {
                compressedData = dcAlg.compress(tokens, catchAlg);
            } catch (DCException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void sequentialTaskRun() {
            try {
                // write bytes length
                int compressedDataLength = compressedData.length;
                for(int i = 0;i < 4;i ++) {
                    out.write((byte)(compressedDataLength & 0xff));
                    compressedDataLength >>= 8;
                }

                // write bytes
                out.write(compressedData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class CompressLastThreadingTask extends SequentialThreadingPool.ThreadingSequentialTask {
        private byte[] buffer;
        private int length;
        private OutputStream out;
        private int blockIndex;
        private int totalBlock;

        public CompressLastThreadingTask(byte[] buffer, int length, OutputStream out, int blockIndex, int totalIndex) {
            this.buffer = buffer;
            this.length = length;
            this.out = out;
            this.blockIndex = blockIndex;
            this.totalBlock = totalIndex;
        }

        @Override
        public void run() {
            // do thing
        }

        @Override
        public void sequentialTaskRun() {
            Notifier.getNotifier().addNotifyMessage(new MSGBlockDCMStartNew(blockIndex, totalBlock));
            try {
                // write remain buffer
                int minusBufferLength = - length;
                for(int i = 0;i < 4;i ++) {
                    out.write((byte)(minusBufferLength & 0xff));
                    minusBufferLength >>= 8;
                }

                // waiting for other jobs
                out.write(buffer, 0, length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

