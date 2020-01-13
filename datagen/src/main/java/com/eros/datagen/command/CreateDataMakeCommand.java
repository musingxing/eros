package com.eros.datagen.command;

import com.eros.datagen.local.LgfPCJob;
import com.eros.job.conf.JobConfig;
import com.eros.shell.command.BaseCommand;
import com.eros.shell.exception.CommandException;
import com.eros.shell.exception.CommandParseException;
import com.eros.shell.exception.MalformedCommandException;
import org.apache.commons.cli.*;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Command for Creating Data Maker.
 *
 * dmaker [OPTIONS...] {COMMAND} ...
 *
 * dmaker create [JOB] {CONFIG}
 * e.g.
 *    dmaker create --type=local --job-name=eros-first-job
 *
 *
 * @author Eros
 * @since   2020-01-06 10:42
 */
public class CreateDataMakeCommand  extends BaseCommand {

    private static Options options = new Options();
    static {
        options.addOption(new Option("j", "job-name",true, "job name"));
        options.addOption(new Option("c", "conf", true, "configuration"));
        options.addOption(new Option("t", "job-type", true, "type"));
        options.addOption(new Option("p", "print-detail", false, "print job detail"));
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
    public CreateDataMakeCommand() {
        super("dmaker", "create");
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

        if(!cl.hasOption("t"))
            System.out.println("\nWARNING: Not set job type and use default: " + JobConfig.JOB_TYPE_NAME.defaultValue());
        String jobType =  cl.hasOption("t") ?  cl.getOptionValue("t") : "local";
        if(!jobTypes.contains(jobType))
            throw new MalformedCommandException("-t argument must be job type in " + jobTypes);

        if(!cl.hasOption("j"))
            System.out.println("\nWARNING: Not set job name and use default: " + JobConfig.JOB_NAME.defaultValue());
        String jobName = options.hasOption("j") ? cl.getOptionValue("j") : (String)JobConfig.JOB_NAME.defaultValue();

        String conf = cl.getOptionValue("c");
        if(conf == null || conf.isEmpty()){
            throw new MalformedCommandException("-c argument must be specified for configuration file");
        }

        File confFile = new File(conf);
        if (!conf.endsWith(".xml")) {
            throw new MalformedCommandException("-c argument specify configuration file " + conf + " is not '.xml'");
        }
        if (!confFile.exists()) {
            throw new MalformedCommandException("-c argument specify configuration file " + conf + " inexistence");
        }
        if (confFile.isDirectory()) {
            throw new MalformedCommandException("-c argument specify configuration file " + conf + " is a directory");
        }

        switch (jobType) {
            case "local": {
                LgfPCJob job = LgfPCJob.buildJob(jobType, jobName, conf);
                job.startup();
                out.println(job.getJobConfig().getTableStr());
                break;
            }
            case "hdfs": {
                err.println("Not support hdfs file system");
                break;
            }

            default: {
                err.println("Unknown file system: " + jobType);
                break;
            }
        }
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
        CreateDataMakeCommand command = new CreateDataMakeCommand();
//        String[] cmdArgs = {"dmaker", "create", "--conf=conf.xml" , "-t", "local"};
        String[] cmdArgs = {"dmaker", "create"};
        command.parse(cmdArgs);
        System.out.println(command.toString());
        System.out.println(command.getUsageStr());
        command.exec();
    }
}
