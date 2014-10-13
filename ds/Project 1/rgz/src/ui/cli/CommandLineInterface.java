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
    private Option optionVersion;
    private Option optionHelp;

    private Option optionListUp;
    private Option optionCompress;
    private Option optionDecompress;
    private Option optionNumThreads;
    private Option optionDCAlgorithm;
    private Option optionCAAlgorithm;
    private Option optionDCM;
    private Option optionBlockDCSize;
    private Option optionFixedBytesCASize;

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

    public void run(String args[]) {
    }
}
