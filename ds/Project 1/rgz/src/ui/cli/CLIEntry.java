package ui.cli;

import ui.UIEntry;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * The Command line interface of the program
 */
public class CLIEntry extends UIEntry {
    private String args[];

    public CLIEntry(String[] args) {
        super();
        this.args = args;
    }

    @Override
    protected void run() {
        CommandLineInterface cli = new CommandLineInterface();
        cli.run(args);
    }

    public static void main(String args[]) {
        CLIEntry entry = new CLIEntry(args);
        entry.start();
    }
}
