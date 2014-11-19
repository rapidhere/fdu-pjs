package ui.cli;

import core.dc.*;
import core.notify.*;
import core.tar.FileNode;
import core.tar.Menu;
import core.tar.RegularFile;
import core.tar.Root;
import excs.RGZException;
import excs.TarException;
import ui.CmdLineParser;
import ui.CmdLineParser.Option;
import ui.Config;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class CommandLineInterface {
    private final static String DC_HUFFMAN = "huffman";
    private final static String DC_RAW = "raw";

    private final static String CA_ASCII = "ascii";
    private final static String CA_FIXED_BYTES_1 = "fixed-byte-1";
    private final static String CA_FIXED_BYTES_2 = "fixed-byte-2";
    private final static String CA_FIXED_BYTES_3 = "fixed-byte-3";
    private final static String CA_FIXED_BYTES_4 = "fixed-byte-4";
    private final static String CA_FIXED_BITS_1 = "fixed-bit-1";
    private final static String CA_FIXED_BITS_2 = "fixed-bit-2";
    private final static String CA_FIXED_BITS_4 = "fixed-bit-4";

    private final static String DCM_BLOCK_64K = "block-64k";
    private final static String DCM_BLOCK_128K = "block-128k";
    private final static String DCM_BLOCK_256K = "block-256k";
    private final static String DCM_BLOCK_512K = "block-512k";
    private final static String DCM_BLOCK_1M = "block-1m";
    private final static String DCM_BLOCK_4M = "block-4m";
    private final static String DCM_BLOCK_16M = "block-16m";

    private CmdLineParser parser = new CmdLineParser();

    // options
    private Option<Boolean> optionVersion;
    private Option<Boolean> optionHelp;

    private Option<Boolean> optionListUp;
    private Option<Boolean> optionCompress;
    private Option<Boolean> optionDecompress;
    private Option<String>  optionDecompressRoot;
    private Option<Integer> optionNumThreads;
    private Option<String>  optionDCAlgorithm;
    private Option<String>  optionCAAlgorithm;
    private Option<String>  optionDCM;

    private Option<Boolean> optionDebug;

    public Config conf = new Config();

    public void loadDCM(String dcmString, int nThread)
        throws RGZException {
        switch (dcmString) {
            case DCM_BLOCK_64K:
                conf.setDcmId(nThread >= 2 ? Config.DCM_BLOCK : Config.DCM_SEQ_BLOCK);
                conf.getFactory().getBlockDCM().setBlockSize(64 * 1024);
                conf.getFactory().getSequentialDCM().setBlockSize(64 * 1024);
                break;
            case DCM_BLOCK_128K:
                conf.setDcmId(nThread >= 2 ? Config.DCM_BLOCK : Config.DCM_SEQ_BLOCK);
                conf.getFactory().getBlockDCM().setBlockSize(128 * 1024);
                conf.getFactory().getSequentialDCM().setBlockSize(128 * 1024);
                break;
            case DCM_BLOCK_256K:
                conf.setDcmId(nThread >= 2 ? Config.DCM_BLOCK : Config.DCM_SEQ_BLOCK);
                conf.getFactory().getBlockDCM().setBlockSize(256 * 1024);
                conf.getFactory().getSequentialDCM().setBlockSize(256 * 1024);
                break;
            case DCM_BLOCK_512K:
                conf.setDcmId(nThread >= 2 ? Config.DCM_BLOCK : Config.DCM_SEQ_BLOCK);
                conf.getFactory().getBlockDCM().setBlockSize(512 * 1024);
                conf.getFactory().getSequentialDCM().setBlockSize(512 * 1024);
                break;
            case DCM_BLOCK_1M:
                conf.setDcmId(nThread >= 2 ? Config.DCM_BLOCK : Config.DCM_SEQ_BLOCK);
                conf.getFactory().getBlockDCM().setBlockSize(1024 * 1024);
                conf.getFactory().getSequentialDCM().setBlockSize(1024 * 1024);
                break;
            case DCM_BLOCK_4M:
                conf.setDcmId(nThread >= 2 ? Config.DCM_BLOCK : Config.DCM_SEQ_BLOCK);
                conf.getFactory().getBlockDCM().setBlockSize(4 * 1024 * 1024);
                conf.getFactory().getSequentialDCM().setBlockSize(4 * 1024 * 1024);
                break;
            case DCM_BLOCK_16M:
                conf.setDcmId(nThread >= 2 ? Config.DCM_BLOCK : Config.DCM_SEQ_BLOCK);
                conf.getFactory().getBlockDCM().setBlockSize(16 * 1024 * 1024);
                conf.getFactory().getSequentialDCM().setBlockSize(16 * 1024 * 1024);
                break;
            default:
                throw new RGZException("unknown dcm " + dcmString);
        }
    }

    public void loadDC(String dcString)
        throws RGZException {
        switch (dcString) {
            case DC_HUFFMAN:
                conf.setDcId(Config.DC_HUFFMAN);
                break;
            case DC_RAW:
                conf.setDcId(Config.DC_RAW);
                break;
            default:
                throw new RGZException("unknown dc-algorithm " + dcString);
        }
    }

    public void loadNotifiers() {
        Notifier notifier = Notifier.getNotifier();

        notifier.register(MSGBlockDCMStartNew.class, msg -> System.out.println(
            "    Processing block: " + msg.getNumberBlock() + "/" + msg.getTotalBlock()));

        notifier.register(MSGTarBuildingIndex.class, msg -> System.out.println(
            "Found source file: " + msg.getPath()));

        notifier.register(MSGDCMCompressNew.class, msg -> {
            double ratio = Math.floor((double)msg.getNFile() / (double)msg.getTotFile() * 100000.0) / 1000.0;
            System.out.println(
                String.format("Compressing %.3f%% %d/%d\n  => %s",
                    ratio, msg.getNFile(), msg.getTotFile(), msg.getPath()));
        });

        notifier.register(MSGDumpingIndex.class, msg -> System.out.println(
            "Dumping index ..."));

        notifier.register(MSGLoadingIndex.class, msg -> System.out.println(
            "Loading index ..."));

        notifier.register(MSGDCMDecompressNew.class, msg -> {
            double ratio = Math.floor((double)msg.getNFile() / (double)msg.getTotFile() * 100000.0) / 1000.0;
            System.out.println(
                String.format("Decompressing %.3f%% %d/%d\n => %s",
                ratio, msg.getNFile(), msg.getTotFile(), msg.getPath()));
        });

        notifier.register(MSGDCMDecompressNewFile.class, msg -> System.out.println("   +> " + msg.getPath()));
    }

    public void loadCA(String caString)
        throws RGZException {
        DCFixedBytesFactory byteFc;
        DCFixedBitsFactory bitFc;
        switch (caString) {
            case CA_ASCII:
                conf.setFcId(Config.FACTORY_ASCII);
                break;
            case CA_FIXED_BYTES_1:
                byteFc = new DCFixedBytesFactory();
                byteFc.getCA().setByteLength((byte )1);
                conf.setFactory(byteFc);
                break;
            case CA_FIXED_BYTES_2:
                byteFc = new DCFixedBytesFactory();
                byteFc.getCA().setByteLength((byte )2);
                conf.setFactory(byteFc);
                break;
            case CA_FIXED_BYTES_3:
                byteFc = new DCFixedBytesFactory();
                byteFc.getCA().setByteLength((byte )3);
                conf.setFactory(byteFc);
                break;
            case CA_FIXED_BYTES_4:
                byteFc = new DCFixedBytesFactory();
                byteFc.getCA().setByteLength((byte )4);
                conf.setFactory(byteFc);
                break;
            case CA_FIXED_BITS_1:
                bitFc = new DCFixedBitsFactory();
                bitFc.getCA().setBitLength((byte) 1);
                conf.setFactory(bitFc);
                break;
            case CA_FIXED_BITS_2:
                bitFc = new DCFixedBitsFactory();
                bitFc.getCA().setBitLength((byte )2);
                conf.setFactory(bitFc);
                break;
            case CA_FIXED_BITS_4:
                bitFc = new DCFixedBitsFactory();
                bitFc.getCA().setBitLength((byte )4);
                conf.setFactory(bitFc);
                break;
            default:
                throw new RGZException("unknown ca-algorithm " + caString);
        }
    }

    public CommandLineInterface() {
        // add arguments
        optionVersion           = parser.addBooleanOption('v', "version");
        optionHelp              = parser.addBooleanOption('h', "help");

        optionListUp            = parser.addBooleanOption('l', "list");
        optionCompress          = parser.addBooleanOption('c', "compress");
        optionDecompress        = parser.addBooleanOption('d', "decompress");
        optionDecompressRoot    = parser.addStringOption('o', "output");
        optionNumThreads        = parser.addIntegerOption('j', "threads");
        optionDCAlgorithm       = parser.addStringOption("dc-algorithm");
        optionCAAlgorithm       = parser.addStringOption("ca-algorithm");
        optionDCM               = parser.addStringOption("dcm");

        optionDebug             = parser.addBooleanOption("debug");
    }

    public void printHelp() {
        System.out.println("Hello World! this is help info!");
        System.out.println();
    }

    public void printVersion() {
        System.out.println("Copyright: (C)RapiD is a JOKER rapidhere@gmail.com");
        System.out.println("RGZ - RapiD's GZ Demo, " + Config.version);
        System.out.println("For any issues or bugs, please contact me rapidhere@gmail.com");
        System.out.println();
    }

    private void printIndent(int depth, boolean noTailFlag) {
        if(depth == 0)
            return;

        for(int i = 0;i < depth - 1;i ++) {
            System.out.print(" |  ");
        }

        if(! noTailFlag)
            System.out.print(" |--");
        else
            System.out.print(" |  ");
    }

    public void listUp(FileNode fn, int depth) {
        if(fn instanceof RegularFile) {
            printIndent(depth, false);
            System.out.println(fn.getName());
        } else {
            Menu mfn = (Menu)fn;
            printIndent(depth, false);
            System.out.println(fn.getName() + "/");
            // print menu first
            boolean firstMenu = true;
            FileNode[] fns = mfn.getChildren();
            Arrays.sort(fns);

            for(FileNode ch: fns) {
                if(ch instanceof Menu) {
                    if(! firstMenu) {
                        printIndent(depth + 1, true);
                        System.out.println();
                    }
                    firstMenu = false;

                    listUp(ch, depth + 1);
                }
            }
            // then print regular file
            boolean firstRegularFile = true;
            for(FileNode ch: fns) {
                if(ch instanceof RegularFile) {
                    if(firstRegularFile && ! firstMenu) {
                        printIndent(depth + 1, true);
                        System.out.println();
                    }
                    firstRegularFile = false;
                    listUp(ch, depth + 1);
                }
            }
        }

    }

    public void run(String args[]) {
        boolean debugFlag = false;

        try {
            parser.parse(args);

            if(parser.getOptionValue(optionVersion, false)) { // show version
                printVersion();
                System.exit(0);
            }

            if(parser.getOptionValue(optionHelp, false)) { // show help
                printHelp();
                System.exit(0);
            }

            loadNotifiers();

            debugFlag = parser.getOptionValue(optionDebug, false);
            if(debugFlag) {
                System.err.println("Debugging mode on ...");
            }

            String[] files = parser.getRemainingArgs();

            if(files.length == 0) {
                System.err.println("Nothing to do!");
                System.exit(1);
            }

            // get source tar file and target files
            String srcFile = files[0];
            String[] targetFiles = new String[files.length - 1];
            System.arraycopy(files, 1, targetFiles, 0, files.length - 1);

            // load root
            Root root = new Root();

            if(parser.getOptionValue(optionCompress, false)) { // do compress
                for(String t: targetFiles)
                    root.addSource(t);

                // load strings
                String dcString = parser.getOptionValue(optionDCAlgorithm, DC_HUFFMAN);
                String caString = parser.getOptionValue(optionCAAlgorithm, CA_ASCII);
                String dcmSting = parser.getOptionValue(optionDCM, DCM_BLOCK_256K);

                // load dc
                int nThread = parser.getOptionValue(optionNumThreads, 0);
                loadCA(caString);
                loadDC(dcString);
                loadDCM(dcmSting, nThread);

                root.compress(srcFile, conf, nThread);
                System.exit(0);
            } else if(parser.getOptionValue(optionDecompress, false)) {
                root.loadIndexFromFile(srcFile);

                // do decompress
                String outputRoot = parser.getOptionValue(optionDecompressRoot, ".");

                // add file node
                ArrayList<FileNode> fn = new ArrayList<>();
                for(String t: targetFiles) {
                    FileNode f = root.findFileNode(t);
                    if(f == null) {

                        throw new TarException("cannot found target file " + t);
                    }
                    fn.add(f);
                }

                // decompress
                root.decompress(outputRoot, fn.toArray(new FileNode[fn.size()]), srcFile);

                System.exit(0);
            } else if(parser.getOptionValue(optionListUp, false)) {
                root.loadIndexFromFile(srcFile);
                // list up
                for(String t: targetFiles) {
                    FileNode fn = root.findFileNode(t);
                    if(fn == null)
                        throw new TarException("cannot list: " + t +" , not in the index");
                    System.out.println("List up =>" + fn.getPath());
                    listUp(fn, 0);
                    System.out.println();
                }

                System.exit(0);
            } else {
                System.err.println("Nothing to do!");
                System.exit(1);
            }
        } catch (Exception e) {
            if(! debugFlag)
                System.err.println(e.getMessage());
            else
                e.printStackTrace();
            System.exit(-1);
        }
    }
}