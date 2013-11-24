package bin.ui.textbaseui;

import bin.Env;
import bin.exp.ArgumentError;
import bin.exp.CommandNotFound;
import bin.exp.MatchMoreThanOneCommand;
import bin.io.IOHelper;
import bin.ui.generic.GenericMapUI;
import bin.ui.generic.Command;
import bin.ui.textbaseui.cmd.CommandParser;
import bin.widget.BMContainer;
import bin.widget.BMNull;
import bin.widget.BMTargetBlock;
import bin.widget.BMWidget;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :      TextBaseMapUI
 * Version :    ver 0.1
 * Usage :      the text-based map ui
 */
public class TextBaseMapUI extends GenericMapUI {
    private Command cmd;    // last Command

    // Tokens
    public static final char
        TOKEN_BOX = '□',
        TOKEN_EMPTY = ' ',
        TOKEN_NULL = ' ',
        TOKEN_PERSON = '☆',
        TOKEN_TARGET_BLOCK_FILLED = '●',
        TOKEN_TARGET_BLOCK_EMPTY = '○',
        TOKEN_WALL = '■';

    public void putError(String err) {
        IOHelper.putStringLine(err);
    }

    public Command getCommand() {
        return cmd;
    }

    /**
     * Map from specified widget to token
     * @param w widget to draw
     * @return the token of widget
     */
    public char getToken(BMWidget w) {
        if(w.getTypeId() == Env.BLOCK_TYPE_NULL) {  // NULL BLOCK
            return TOKEN_NULL;
        } else if(w.getTypeId() == Env.BLOCK_TYPE_TARGET) { // TARGET BLOCK
            BMTargetBlock t = (BMTargetBlock)w;

            // filled or has nothing
            // or draw inner
            if(t.isFilled()) {
                return TOKEN_TARGET_BLOCK_FILLED;
            } else if(w.isPassable()) {
                return TOKEN_TARGET_BLOCK_EMPTY;
            }
        } else if(w.getTypeId() == Env.BLOCK_TYPE_WALL ) { // WALL BLOCK
            return TOKEN_WALL;
        }

        // Containers
        if(w.isPassable()) {
            return TOKEN_EMPTY;
        } else { // draw inner
            w = ((BMContainer)w).getInner();
            switch (w.getTypeId()) {
                case Env.BLOCK_TYPE_PERSON: return TOKEN_PERSON;
                case Env.BLOCK_TYPE_BOX:    return TOKEN_BOX;
            }
        }
        return TOKEN_EMPTY;
    }

    public void draw() {
        // Draw steps
        IOHelper.putStringLine("Steps: " + map.getCurrentStep());

        // Draw info
        if(info != null) {
            IOHelper.putStringLine(info);
        }

        // Draw map
        for(int i = 0;i < map.getHeight();i ++) {
            for(int j = 0;j < map.getWidth();j ++)
                IOHelper.putChar(getToken(map.getWidgetAt(i, j)));
            IOHelper.putEOL();
        }

        // Parse Command
        CommandParser cp = CommandParser.getCommandParser();
        while (true) {
            try {
                IOHelper.putString(">>> ");
                cmd = cp.parseCommandLine(IOHelper.getLowerStringLine());
                break;
            } catch (CommandNotFound cnf) {
                putError("Cannot found cmd: " + cnf.getCommandName());
            } catch (ArgumentError ae) {
                putError(ae.getExString());
            } catch (MatchMoreThanOneCommand m) {
                String es = "Command " + m.getCmdName() + " Matched more than one command:\n";
                for(int i = 0;i < m.getMatchedSize();i ++)
                    es += "\t" + m.get(i);
                putError(es);
            }
        }
        IOHelper.putSolidLine();
    }
}
