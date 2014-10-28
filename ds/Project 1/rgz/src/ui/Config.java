package ui;

import core.dc.*;
import excs.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 */
final public class Config {
    public static String version = "ver 0.1";

    private DCFactory factory;
    private byte dcmId;
    private byte dcId;

    /**
     * get the factory of config
     * @return the factory
     */
    public DCFactory getFactory() {
        return factory;
    }

    /**
     * get the dcm
     * @return the dcm
     */
    public DCM getDCM() {
        return getFactory().getDCM(dcmId, dcId);
    }

    /**
     * set the factory of config
     * @param factory the factory
     */
    public void setFactory(DCFactory factory) {
        this.factory = factory;
    }

    /**
     * set the factory id
     * @param fcId the factory id
     * @throws UnknownFactoryId
     */
    public void setFcId(byte fcId) throws UnknownFactoryId {
        try {
            this.factory = getFactoryById(fcId).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * set the dc id
     * @param dcId the dc id
     * @throws UnknownDCAlgorithmId
     */
    public void setDcId(byte dcId)
    throws UnknownDCAlgorithmId {
        switch (dcId) {
            case DC_HUFFMAN: this.dcId = dcId; return;
            case DC_RAW: this.dcId = dcId; return;
        }
        throw new UnknownDCAlgorithmId(dcId);
    }

    /**
     * set the dcm id
     * @param dcmId dcm id
     * @throws UnknownDCMId
     */
    public void setDcmId(byte dcmId)
    throws UnknownDCMId {
        switch (dcmId) {
            case DCM_BLOCK: this.dcmId = dcmId; return;
            case DCM_SEQ_BLOCK: this.dcmId = dcmId; return;
        }

        throw new UnknownDCMId(dcmId);
    }

    /**
     * dump config info to output stream
     * @param out the output stream
     * @throws DCException
     */
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

    /**
     * load meta from input stream
     * @param in the input stream
     * @throws DCException
     */
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
    public static final byte DCM_SEQ_BLOCK = 1;
}
