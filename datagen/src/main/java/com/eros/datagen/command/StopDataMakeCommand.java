package com.eros.datagen.command;

import com.eros.job.PCJob;
import com.eros.job.conf.JobConfig;
import com.eros.job.manager.JobManager;
import com.eros.shell.command.BaseCommand;
import com.eros.shell.exception.CommandException;
import com.eros.shell.exception.CommandParseException;
import org.apache.commons.cli.*;

/**
 * Command for Stop Data Maker Job.
 *
 * dmaker [OPTIONS...] {COMMAND} ...
 *
 * dmaker stop [JOB] {JOB_NAME}
 * e.g.
 *    dmaker stop --type=local --job-name=eros-first-job
 *
 * @author Eros
 * @since   2020-01-06 10:42
 */
public class StopDataMakeCommand extends BaseCommand {

    private static Options options = new Options();
    static {
        options.addOption(new Option("p", "print", false, "print result"));
        options.addOption(new Option("t", "type",true, "job type"));
        options.addOption(new Option("j", "job-name",true, "job name"));
    }

    private String[] args;
    private CommandLine cl;

    /**
     * Constructor
     */
    public StopDataMakeCommand() {
        super("dmaker", "stop");
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

        if(!cl.hasOption("j"))
            System.out.println("\n WARNING: Not set job name and use default: " + (String)JobConfig.JOB_NAME.defaultValue());
        String jobName = cl.hasOption("j") ? cl.getOptionValue("t") : (String)JobConfig.JOB_NAME.defaultValue();
        String jobType = cl.hasOption("t") ? cl.getOptionValue("t") : (String)JobConfig.JOB_TYPE_NAME.defaultValue();
        PCJob job = JobManager.queryJob(jobType, jobName);
        if(job == null){
            System.out.println(String.format("\nNot found job<type=%s, name=%s>", jobType, jobName));
            return false;
        }

        if(cl.hasOption("p"))
            System.out.println(String.format("\nJob<type=%s, name=%s>\n%s", jobType, jobName, job.getJobInfo()));

        return true;
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
        BaseCommand command = new StopDataMakeCommand();
        String[] cmdArgs = {"dmaker", "stop"};
        command.parse(cmdArgs);
        System.out.println(command.toString());
        System.out.println(command.getUsageStr());
        command.exec();
    }
}
