package com.eros.shell.command;


import com.eros.shell.exception.CommandException;
import com.eros.shell.exception.CommandParseException;
import org.apache.commons.cli.Option;

import java.io.PrintStream;
import java.util.*;

/**
 * base class for all commands
 */
public abstract class BaseCommand {

    private static Map<String, BaseCommand> COMMAND_INSTANCES = new HashMap<String, BaseCommand>();
    protected PrintStream out;
    protected PrintStream err;
    private String cmdStr;
    private String optionStr;

    /**
     * a CLI command with command string and options.
     * Using System.out and System.err for printing
     * @param cmdStr the string used to call this command
     * @param optionStr the string used to call this command
     */
    public BaseCommand(String cmdStr, String optionStr) {
        this.out = System.out;
        this.err = System.err;
        this.cmdStr = cmdStr;
        this.optionStr = optionStr;
    }

    /**
     * Set out printStream (usable for testing)
     * @param out
     */
    public void setOut(PrintStream out) {
        this.out = out;
    }

    /**
     * Set err printStream (usable for testing)
     * @param err
     */
    public void setErr(PrintStream err) {
        this.err = err;
    }

    /**
     * get the string used to call this command
     * @return
     */
    public String getCmdStr() {
        return cmdStr;
    }

    /**
     * get the option string
     * @return
     */
    public String getOptionStr() {
        return optionStr;
    }

    /**
     * get a usage string, contains the command and the options
     * @return
     */
    public String getUsageStr() {
        return getCmdAndOpStr();
    }

    /**
     * get cmd string, contains the command and the option
     * @return
     */
    public String getCmdAndOpStr() {
        return cmdStr+(optionStr==null||optionStr.isEmpty() ? "" : " "+optionStr);
    }

    /**
     * add this command to a map. Use the command string as key.
     */
    public void addToMap() {
        COMMAND_INSTANCES.put(getCmdAndOpStr(), this);
    }

    /**
     * parse the command arguments
     * @param cmdArgs
     * @return this CliCommand
     * @throws CommandParseException
     */
    public abstract BaseCommand parse(String[] cmdArgs) throws CommandParseException;

    /**
     *
     * @return
     * @throws CommandException
     */
    public abstract boolean exec() throws CommandException;

    public static List<BaseCommand> getCommands(){
        return new ArrayList<>(COMMAND_INSTANCES.values());
    }

    public static BaseCommand getCommand(String command){
        return COMMAND_INSTANCES.get(command);
    }

    /**
     * Getter usage information
     *
     * @param cmd             command
     * @param optionStr       option
     * @param options         option args
     * @return                Usage description
     */
    public static String genUsageStr(String cmd, String optionStr, Collection<Option> options){
        StringBuilder builder = new StringBuilder();
        builder.append("\t").append(cmd).append("  ").append(optionStr==null||optionStr.isEmpty()?"":"["+optionStr+"]");
        if(options != null){
            for(Option option : options){
                builder.append("\n").append("\t\t").append("-").append(option.getOpt())
                        .append("\t").append("--").append(String.format("%.16s", option.getLongOpt()))
                        .append("\t").append(option.getDescription());
            }
        }
        return builder.toString();
    }

    /**
     * Command string represent
     *
     * @param args            args array
     * @param options         option list
     * @return                string represent
     */
    public static String commandToString(String[] args, Option[] options){
        StringBuilder builder = new StringBuilder("[ ");
        if(args != null && args.length > 0){
            for(String arg : args){
                builder.append(arg).append(" ");
            }
        }

        if(options != null ){
            for(Option option : options){
                String desc =  "--" + option.getLongOpt() + "=" + option.getValue()+" ";
                builder.append(desc);
            }
        }
        return builder.append("]").toString();
    }
}
