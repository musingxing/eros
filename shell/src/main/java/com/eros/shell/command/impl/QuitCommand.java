package com.eros.shell.command.impl;

import com.eros.shell.command.BaseCommand;
import com.eros.shell.exception.CommandException;
import com.eros.shell.exception.CommandParseException;
import org.apache.commons.cli.*;

/**
* Quit Command.
 *
 * @author Eros
 * @since 2020-01-06 10:42
 */
public class QuitCommand extends BaseCommand {

    private CommandLine cl;
    private String[] args;

    /**
     * Constructor
     */
    public QuitCommand() {
        super("quit", "");
    }

    @Override
    public BaseCommand parse(String[] cmdArgs) throws CommandParseException {
        Parser parser = new PosixParser();
        try {
            cl = parser.parse(new Options(), cmdArgs);
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
        System.exit(0);
        return true;
    }

    @Override
    public String getUsageStr() {
        return genUsageStr(getCmdStr(), getOptionStr(), null);
    }

    @Override
    public String toString() {
        return commandToString(args, cl==null ? null : cl.getOptions());
    }

    public static void main(String[] args) throws Exception {
        String[] cmdArgs = new String[]{"quit"};

        QuitCommand command = new QuitCommand();
        command.parse(cmdArgs);
        System.out.println(command);
        System.out.println(command.getUsageStr());
        command.exec();
    }
}
