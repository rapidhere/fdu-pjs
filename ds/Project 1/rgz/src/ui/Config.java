package ui;

import core.dc.*;
import excs.*;

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
final public class Config {
    public static String version = "ver 0.1";

    private DCFactory factory;
    private byte dcmId;
    private byte dcId;

    public DCFactory getFactory() {
        return factory;
    }
    public DCM getDCM() {
        return getFactory().getDCM(dcmId, dcId);
    }

    public void setFactory(DCFactory factory) {
        this.factory = factory;
    }

    public void setFcId(byte fcId) throws UnknownFactoryId {
        try {
            this.factory = getFactoryById(fcId).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setDcId(byte dcId)
    throws UnknownDCAlgorithmId {
        switch (dcId) {
            case DC_HUFFMAN: this.dcId = dcId; return;
            case DC_RAW: this.dcId = dcId; return;
        }
        throw new UnknownDCAlgorithmId(dcId);
    }

    public void setDcmId(byte dcmId)
    throws UnknownDCMId {
        switch (dcmId) {
            case DCM_BLOCK: this.dcmId = dcmId; return;
        }

        throw new UnknownDCMId(dcmId);
    }

    public void dumpMeta(OutputStream out)
    throws DCException {
        try {
            out.write(getFactoryId(factory.getClass()));
            out.write(dcmId);
            out.write(dcId);
            factory.getCA().dumpHeader(out);
        } catch (IOException e) {
            throw new DCException(e.getMessage());
        } catch (UnknownFactory unknownFactory) {
            // never reach here
            unknownFactory.printStackTrace();
        }
    }

    public void loadMeta(InputStream in)
    throws DCException {
        try {
            setFcId((byte) in.read());
            setDcmId((byte) in.read());
            setDcId((byte) in.read());
            factory.getCA().loadHeader(in);
        } catch (IOException | UnknownFactoryId | UnknownDCMId | UnknownDCAlgorithmId e) {
            throw new DCException(e.getMessage());
        }
    }

    // DCM Algorithm
    // Huffman tree algorithm
    public static final byte DC_HUFFMAN = 0;
    public static final byte DC_RAW = 1;

    // Factory IDs
    public static final byte FACTORY_ASCII = 0;
    public static final byte FACTORY_FIXED_BIT = 1;
    public static final byte FACTORY_FIXED_BYTE = 2;

    /**
     * get ca id by ca
     * @param fcClass the catch algorithm
     * @return the id of the ca
     * @throws excs.UnknownFactory
     */
    public static byte getFactoryId(Class<? extends DCFactory> fcClass)
    throws UnknownFactory {
        if(fcClass.equals(DCASCIIFactory.class)) {
            return FACTORY_ASCII;
        } else if(fcClass.equals(DCFixedBitsFactory.class)) {
            return FACTORY_FIXED_BIT;
        } else if(fcClass.equals(DCFixedBytesFactory.class)) {
            return FACTORY_FIXED_BYTE;
        }
        throw new UnknownFactory(fcClass);
    }

    /**
     * get the catch algorithm by id
     * @param fcId the catch algorithm
     * @return the catch algorithm
     * @throws excs.UnknownFactoryId
     */
    public static Class<? extends DCFactory> getFactoryById(byte fcId)
    throws UnknownFactoryId {
        switch (fcId) {
            case FACTORY_ASCII: return DCASCIIFactory.class;
            case FACTORY_FIXED_BIT: return DCFixedBitsFactory.class;
            case FACTORY_FIXED_BYTE: return DCFixedBitsFactory.class;
            default: throw new UnknownFactoryId(fcId);
        }
    }

    // DC_MAN
    public static final byte DCM_BLOCK = 0;
}
