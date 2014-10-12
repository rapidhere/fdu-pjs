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
    private Option optionOutputFile;

    public CommandLineInterface() {
        // add arguments
        // output file
        optionOutputFile = parser.addStringOption('o', "output");
    }

    public void run(String args[]) {
        // create dc
        CatchFixedBytesAlgorithm ca = new CatchFixedBytesAlgorithm();
        ca.setByteLength((byte) 1);
        DC dc = new BlockDC(new DCHuffmanAlgorithm(), ca, 256 * 1024);

        try {
            dc.decompress(
                new FileInputStream("/home/rapid/work/a-2byte.rgz"),
                new FileOutputStream("/home/rapid/work/b.txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
