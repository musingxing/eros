package com.eros.datagen.command;

import com.eros.common.util.LoggerUtil;
import com.eros.datagen.local.LgfJobConfig;
import com.eros.datagen.local.LgfPCJob;
import com.eros.job.PCJob;
import com.eros.job.conf.JobConfig;
import com.eros.shell.command.BaseCommand;
import com.eros.shell.exception.CommandException;
import com.eros.shell.exception.CommandParseException;
import com.eros.shell.exception.MalformedCommandException;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.*;

/**
 * Command for Creating Data Maker.
 *
 * dmaker [OPTIONS...] {COMMAND} ...
 *
 * dmaker create [JOB] {CONFIG}
 * e.g.
 *    dmaker create -local eros-first-job /myjob/config.xml
 *
 *
 * @author Eros
 * @since   2020-01-06 10:42
 */
public class StopDataMakeCommand extends BaseCommand {

    private static final Logger logger = LoggerUtil.getLogger("command", StopDataMakeCommand.class);
    private static Options options = new Options();
    static {
        options.addOption(new Option("p", "print-", false, "print result"));
    }
    private static Set<String> jobTypes = new HashSet<>();
    static {
        jobTypes.add("local");
        jobTypes.add("hdfs");
    }

    private String[] args;
    private CommandLine cl;

    /**
     * Constructor
     */
    public StopDataMakeCommand() {
        super("dmaker stop", " ");
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
        if (args.length < 2) {
            throw new CommandParseException(getUsageStr());
        }
        return this;
    }

    @Override
    public boolean exec() throws CommandException {

        logger.info("Command Exec: " + toString());
        // stop job
        PCJob job = null;

        boolean print = cl.hasOption("p");
        if(print){
            out.println(job.getJobConfig());
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        return "CreateDataMakeCommand{" +
                "args=" + Arrays.toString(args) +
                ", cl=" + cl +
                '}';
    }

    public static void main(String[] args) throws Exception {
        StopDataMakeCommand command = new StopDataMakeCommand();
        String[] cmdArgs = {"dmaker", "stop"};
        BaseCommand baseCommand = command.parse(cmdArgs);
        System.out.println(baseCommand.toString());
        baseCommand.exec();
    }
}
