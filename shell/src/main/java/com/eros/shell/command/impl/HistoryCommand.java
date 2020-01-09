package com.eros.shell.command.impl;

import com.eros.shell.command.BaseCommand;
import com.eros.shell.exception.CommandException;
import com.eros.shell.exception.CommandParseException;
import org.apache.commons.cli.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Command to record processed command.
 *
 * @author Eros
 * @since 2020-01-06 10:42
 */
public class HistoryCommand extends BaseCommand {

    private static Options options = new Options();

    static {
        options.addOption(new Option("n", "number", true, "last number of command"));
    }

    private Map<Integer, String> history = new HashMap<Integer, String>();
    private int commandCount = 0;
    private String[] args;
    private CommandLine cl;

    /**
     * Constructor
     */
    public HistoryCommand() {
        super("history", null);
    }

    @Override
    public BaseCommand parse(String[] cmdArgs) throws CommandParseException {
        Parser parser = new PosixParser();
        try {
            cl = parser.parse(options, cmdArgs);
        } catch (ParseException ex) {
            throw new CommandParseException(ex);
        }
        args = cl.getArgs();
        if (args.length < 1) {
            throw new CommandParseException(getUsageStr());
        }
        return this;
    }

    @Override
    public boolean exec() throws CommandException {
        String countStr = cl.hasOption("c") ? cl.getOptionValue("c") : null;
        try {
            int count = countStr == null ? 10 : Integer.parseInt(countStr);
            for(int start = (count<history.size()?history.size()-count+1:history.size()); start <=history.size();  start++){
                String cmd = history.get(start);
                if(cmd != null)
                    System.err.println(cmd);
            }
        } catch (NumberFormatException e) {
            throw new CommandException("history [-n number], '" + countStr + "' is not number");
        }
        return true;
    }

    /**
     * Makes a list of possible completions, either for commands
     * or for zk nodes if the token to complete begins with /
     */
    public void addToHistory(String cmd) {
        history.put((++commandCount), cmd);
    }

    public int getCommandCount() {
        return commandCount;
    }

    @Override
    public String getUsageStr() {
        return genUsageStr(getCmdStr(), getOptionStr(), options.getOptions());
    }

    @Override
    public String toString() {
       return commandToString(args, cl==null ? null : cl.getOptions());
    }

    public static void main(String[] args) throws Exception {
        String[] cmdArgs = new String[]{"history -n 3"};
        HistoryCommand command = new HistoryCommand();
        command.parse(cmdArgs);
        command.exec();
        System.out.println(command);
        System.out.println(command.getUsageStr());
    }
}
