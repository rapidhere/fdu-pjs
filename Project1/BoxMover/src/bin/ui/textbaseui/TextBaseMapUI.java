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
 * Class :
 * Version :
 * Usage :
 */
public class TextBaseMapUI extends GenericMapUI {
    private Command cmd;

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

    public char getToken(BMWidget w) {
        if(w.getTypeId() == Env.BLOCK_TYPE_NULL) {
            return TOKEN_NULL;
        } else if(w.getTypeId() == Env.BLOCK_TYPE_TARGET) {
            BMTargetBlock t = (BMTargetBlock)w;
            if(t.isFilled()) {
                return TOKEN_TARGET_BLOCK_FILLED;
            } else if(w.isPassable()) {
                return TOKEN_TARGET_BLOCK_EMPTY;
            }
        } else if(w.getTypeId() == Env.BLOCK_TYPE_WALL ) {
            return TOKEN_WALL;
        }

        if(w.isPassable()) {
            return TOKEN_EMPTY;
        } else {
            w = ((BMContainer)w).getInner();
            switch (w.getTypeId()) {
                case Env.BLOCK_TYPE_PERSON: return TOKEN_PERSON;
                case Env.BLOCK_TYPE_BOX:    return TOKEN_BOX;
            }
        }
        return TOKEN_EMPTY;
    }

    public void draw() {
        IOHelper.putStringLine("Steps: " + cur_step);
        if(info != null) {
            IOHelper.putStringLine(info);
        }

        for(int i = 0;i < map.getHeight();i ++) {
            for(int j = 0;j < map.getWidth();j ++)
                IOHelper.putChar(getToken(map.getWidgetAt(i, j)));
            IOHelper.putEOL();
        }

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
