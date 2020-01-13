package com.eros.shell.command.impl;

import com.eros.common.string.DoubleColumnTable;
import com.eros.shell.command.BaseCommand;
import com.eros.shell.exception.CommandException;
import com.eros.shell.exception.CommandParseException;

import java.util.*;

/**
* Help Command.
 *
 * @author Eros
 * @since 2020-01-06 10:42
 */
public class HelpCommand extends BaseCommand {

    private String[] args;
    private final DoubleColumnTable table = DoubleColumnTable.newTable('-', '|', 128, "COMMANDS", "OPTIONS");

    /**
     * Constructor
     */
    public HelpCommand() {
        super("help", "");
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
                System.out.println(table.getHeader()+ "\n" + command.getUsageStr());
            }
            System.out.println(table.getHeader());
            return true;
        }

        List<BaseCommand> commandList = new ArrayList<>();
        for (BaseCommand command : commands) {
            if (Objects.equals(command.getCmdStr(), args[1]))
                commandList.add(command);
        }
        Collections.sort(commandList, new Comparator<BaseCommand>() {
            @Override
            public int compare(BaseCommand o1, BaseCommand o2) {
                return o1.getCmdStr().compareTo(o2.getCmdStr());
            }
        });
        for (BaseCommand command : commandList) {
            System.out.println(table.getHeader()+ "\n" + command.getUsageStr());
        }
        System.out.println(table.getHeader());
        return true;
    }

    @Override
    public String getUsageStr() {
        return genUsageStr(getCmdStr(), "command", null);
    }

    @Override
    public String toString() {
        return commandToString(args, null);
    }

    public static void main(String[] args) throws Exception {
        String[] cmdArgs = new String[]{"help"};

        HelpCommand command = new HelpCommand();
        command.parse(cmdArgs);
        System.out.println(command);
        System.out.println(command.getUsageStr());
        command.exec();
    }
}
