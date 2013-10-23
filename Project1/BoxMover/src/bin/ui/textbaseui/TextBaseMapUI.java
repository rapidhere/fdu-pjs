package bin.ui.textbaseui;

import bin.exp.ArgumentError;
import bin.exp.CommandNotFound;
import bin.exp.MatchMoreThanOneCommand;
import bin.io.IOHelper;
import bin.ui.generic.GenericMapUI;
import bin.ui.generic.Command;
import bin.ui.textbaseui.cmd.CommandParser;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */
public class TextBaseMapUI extends GenericMapUI {
    private Command cmd;
    public void putError(String err) {
        IOHelper.putStringLine(err);
    }

    public Command getCommand() {
        return cmd;
    }

    public void draw() {
        IOHelper.putStringLine("Steps: " + cur_step);
        if(info != null) {
            IOHelper.putStringLine(info);
        }

        for(int i = 0;i < map.getHeight();i ++) {
            for(int j = 0;j < map.getWidth();j ++)
                IOHelper.putChar(map.getWidgetAt(i, j).getToken());
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
