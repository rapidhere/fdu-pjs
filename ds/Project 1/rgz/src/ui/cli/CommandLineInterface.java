package ui.cli;

import core.dc.*;
import core.tar.FileNode;
import core.tar.Menu;
import core.tar.RegularFile;
import core.tar.Root;
import excs.RGZException;
import excs.TarException;
import ui.CmdLineParser;
import ui.CmdLineParser.Option;

import java.util.ArrayList;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class CommandLineInterface {
    private final static String DC_HUFFMAN = "huffman";

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

    public static DCM loadDCM(String dcmString, DCAlgorithm dc, CatchAlgorithm ca) {
        if(dcmString.equals(DCM_BLOCK_64K)) {
            return new BlockDCM(dc, ca, 64 * 1024);
        } else if(dcmString.equals(DCM_BLOCK_128K)) {
            return new BlockDCM(dc, ca, 128 * 1024);
        } else if(dcmString.equals(DCM_BLOCK_256K)) {
            return new BlockDCM(dc, ca, 256 * 1024);
        } else if(dcmString.equals(DCM_BLOCK_512K)) {
            return new BlockDCM(dc, ca, 512 * 1024);
        } else if(dcmString.equals(DCM_BLOCK_1M)) {
            return new BlockDCM(dc, ca, 1024 * 1024);
        } else if(dcmString.equals(DCM_BLOCK_4M)) {
            return new BlockDCM(dc, ca, 4 * 1024 * 1024);
        } else if(dcmString.equals(DCM_BLOCK_16M)) {
            return new BlockDCM(dc, ca, 16 * 1024 * 1024);
        }

        return null;
    }

    public static DCAlgorithm loadDC(String dcString) {
        if(dcString.equals(DC_HUFFMAN)) {
            return new DCHuffmanAlgorithm();
        }

        return null;
    }

    public static CatchAlgorithm loadCA(String caString) {
        if(caString.equals(CA_ASCII)) {
            return new CatchASCIIAlgorithm();
        } else if(caString.equals(CA_FIXED_BYTES_1)) {
            return new CatchFixedBytesAlgorithm((byte) 1);
        } else if(caString.equals(CA_FIXED_BYTES_2)) {
            return new CatchFixedBytesAlgorithm((byte) 2);
        } else if(caString.equals(CA_FIXED_BYTES_3)) {
            return new CatchFixedBytesAlgorithm((byte) 3);
        } else if(caString.equals(CA_FIXED_BYTES_4)) {
            return new CatchFixedBytesAlgorithm((byte) 4);
        } else if(caString.equals(CA_FIXED_BITS_1)) {
            return new CatchFixedBitsAlgorithm((byte) 1);
        } else if(caString.equals(CA_FIXED_BITS_2)) {
            return new CatchFixedBitsAlgorithm((byte) 2);
        } else if(caString.equals(CA_FIXED_BITS_4)) {
            return new CatchFixedBitsAlgorithm((byte) 4);
        }

        return null;
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
        System.out.println("RGZ - RapiD's GZ Demo, ver 0.1");
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
            for(FileNode ch: mfn.getChildren()) {
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
            for(FileNode ch: mfn.getChildren()) {
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

                // load up dc ca dcm
                DCM dcm;
                DCAlgorithm dc;
                CatchAlgorithm ca;

                // load strings
                String dcString = parser.getOptionValue(optionDCAlgorithm, DC_HUFFMAN);
                String caString = parser.getOptionValue(optionCAAlgorithm, CA_ASCII);
                String dcmSting = parser.getOptionValue(optionDCM, DCM_BLOCK_256K);

                // load dc
                dc = loadDC(dcString);
                if(dc == null)
                    throw new RGZException("unknown dc-algorithm " + dcString);
                ca = loadCA(caString);
                if(ca == null)
                    throw new RGZException("unknown ca-algorithm " + caString);
                dcm = loadDCM(dcmSting, dc, ca);
                if(dcm == null)
                    throw new RGZException("unknown dcm-algorithm " + dcmSting);

                // compress
                root.compress(srcFile, dcm);
            } else if(parser.getOptionValue(optionDecompress, false)) {
                root.loadIndexFromFile(srcFile);

                // do decompress
                String outputRoot = parser.getOptionValue(optionDecompressRoot, ".");

                // add file node
                ArrayList<FileNode> fn = new ArrayList<FileNode>();
                for(String t: targetFiles) {
                    FileNode f = root.findFileNode(t);
                    if(f == null) {
                        throw new TarException("cannot found target file " + t);
                    }
                    fn.add(f);
                }

                // decompress
                root.decompress(outputRoot, fn.toArray(new FileNode[fn.size()]), srcFile);
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
            } else {
                System.err.println("Nothing to do!");
                System.exit(1);
            }
        } catch (Exception e) {
            if(! debugFlag)
                System.err.println(e.getMessage());
            else
                e.printStackTrace();
        }
    }
}