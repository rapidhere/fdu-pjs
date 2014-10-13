package ui.cli;

import core.dc.*;
import ui.CmdLineParser;
import ui.CmdLineParser.Option;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class CommandLineInterface {
    private CmdLineParser parser = new CmdLineParser();

    // options
    private Option<Boolean> optionVersion;
    private Option<Boolean> optionHelp;

    private Option<Boolean> optionListUp;
    private Option<Boolean> optionCompress;
    private Option<Boolean> optionDecompress;
    private Option<Integer> optionNumThreads;
    private Option<String>  optionDCAlgorithm;
    private Option<String>  optionCAAlgorithm;
    private Option<String>  optionDCM;
    private Option<Integer> optionBlockDCSize;
    private Option<Integer> optionFixedBytesCASize;

    public CommandLineInterface() {
        // add arguments
        optionVersion           = parser.addBooleanOption('v', "version");
        optionHelp              = parser.addBooleanOption('h', "help");

        optionListUp            = parser.addBooleanOption('l', "list");
        optionCompress          = parser.addBooleanOption('c', "compress");
        optionDecompress        = parser.addBooleanOption('d', "decompress");
        optionNumThreads        = parser.addIntegerOption('j', "threads");
        optionDCAlgorithm       = parser.addStringOption("dc-algorithm");
        optionCAAlgorithm       = parser.addStringOption("ca-algorithm");
        optionDCM               = parser.addStringOption("dcm");
        optionBlockDCSize       = parser.addIntegerOption("dcm-block-size");
        optionFixedBytesCASize  = parser.addIntegerOption("ca-fixed-bytes-size");
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

    public void run(String args[]) {
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

            String[] files = parser.getRemainingArgs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
