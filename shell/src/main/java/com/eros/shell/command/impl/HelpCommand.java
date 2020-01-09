package com.eros.shell.command.impl;

import com.eros.shell.command.BaseCommand;
import com.eros.shell.exception.CommandException;
import com.eros.shell.exception.CommandParseException;
import org.apache.commons.cli.*;

import java.util.*;

/**
* Help Command.
 *
 * @author Eros
 * @since 2020-01-06 10:42
 */
public class HelpCommand extends BaseCommand {

    private String[] args;

    /**
     * Constructor
     */
    public HelpCommand() {
        super("help", "command");
    }

    @Override
    public BaseCommand parse(String[] cmdArgs) throws CommandParseException {
        if(cmdArgs == null || cmdArgs.length == 0)
            throw new CommandParseException("Empty command");
        args = cmdArgs;
        return this;
    }

    @Override
    public boolean exec() throws CommandException {
        List<BaseCommand> commands = BaseCommand.getCommands();
        if(args.length == 1){
            Collections.sort(commands, new Comparator<BaseCommand>() {
                @Override
                public int compare(BaseCommand o1, BaseCommand o2) {
                    return o1.getCmdStr().compareTo(o2.getCmdStr());
                }
            });
            for (BaseCommand command : commands) {
                System.out.println("\n" + command.getUsageStr());
            }
        } else {
            List<BaseCommand> commandList = new ArrayList<>();
            for (BaseCommand command : commands) {
                if(Objects.equals(command.getCmdStr(), args[1]))
                    commandList.add(command);
            }
            Collections.sort(commandList, new Comparator<BaseCommand>() {
                @Override
                public int compare(BaseCommand o1, BaseCommand o2) {
                    return o1.getCmdStr().compareTo(o2.getCmdStr());
                }
            });
            for (BaseCommand command : commandList) {
                System.out.println("\n" + command.getUsageStr());
            }
        }
        return true;
    }

    @Override
    public String getUsageStr() {
        return genUsageStr(getCmdStr(), getOptionStr(), null);
    }

    @Override
    public String toString() {
        return commandToString(args, null);
    }

    public static void main(String[] args) throws Exception {
        String[] cmdArgs = new String[]{"help", "command"};

        HelpCommand command = new HelpCommand();
        command.parse(cmdArgs);
        System.out.println(command);
        System.out.println(command.getUsageStr());
        command.exec();
    }
}
