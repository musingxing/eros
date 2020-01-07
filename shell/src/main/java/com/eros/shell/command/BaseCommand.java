package com.eros.shell.command;


import com.eros.shell.exception.CommandException;
import com.eros.shell.exception.CommandParseException;

import java.io.PrintStream;
import java.util.Map;

/**
 * base class for all commands
 */
public abstract class BaseCommand {

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
        return cmdStr + " " + optionStr;
    }

    /**
     * add this command to a map. Use the command string as key.
     * @param cmdMap
     */
    public void addToMap(Map<String, BaseCommand> cmdMap) {
        cmdMap.put(cmdStr, this);
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

}
