package ui.cli;

import core.dc.BlockDC;
import core.dc.CatchASCIIAlgorithm;
import core.dc.DC;
import core.dc.DCHuffmanAlgorithm;
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
    private Option optionOutputFile;

    public CommandLineInterface() {
        // add arguments
        // output file
        optionOutputFile = parser.addStringOption('o', "output");
    }

    public void run(String args[]) {
        // create dc
        DC dc = new BlockDC(new DCHuffmanAlgorithm(), new CatchASCIIAlgorithm(), 256 * 1024);

        try {
            dc.decompress(
                new FileInputStream("/home/rapid/work/a.rgz"),
                new FileOutputStream("/home/rapid/work/a.txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
