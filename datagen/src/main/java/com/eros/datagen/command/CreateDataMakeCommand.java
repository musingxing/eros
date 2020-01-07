package com.eros.datagen.command;

import com.eros.datagen.local.LgfPCJob;
import com.eros.shell.command.BaseCommand;
import com.eros.shell.exception.CommandException;
import com.eros.shell.exception.CommandParseException;
import com.eros.shell.exception.MalformedCommandException;
import org.apache.commons.cli.*;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
    public CreateDataMakeCommand(String cmdStr, String optionStr) {
        super("dmaker create", "[-t type] [-c conf]");
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
        String jobType =  cl.hasOption("t") ?  cl.getOptionValue("t") : "local";
        if(!jobTypes.contains(jobType))
            throw new MalformedCommandException("-t argument must be job type in " + jobTypes);

        String conf = cl.getOptionValue("c");
        if(conf != null && !conf.isEmpty()){
            File confFile = new File(conf);
            if(!conf.endsWith(".xml"))
                throw new MalformedCommandException("-c argument specify configuration file " + conf + " is not '.xml'");
            if(!confFile.exists())
                throw new MalformedCommandException("-c argument specify configuration file "  + conf + " inexistence");
            if(!confFile.isDirectory())
                throw new MalformedCommandException("-c argument specify configuration file " + conf + " is a directory");
        }

        String jobName = options.hasOption("j") ? cl.getOptionValue("j") : null;
        try{
            switch (jobType){
                case "local":{
                    LgfPCJob job = LgfPCJob.buildJob(jobName, conf);
                    job.startup();
                    out.println(job.getJobConfig());
                    break;
                }
                case "hdfs":{
                    err.println("Not support hdfs file system");
                    break;
                }

                default:{
                    err.println("Unknown file system: " + jobType);
                    break;
                }
            }
        }catch (Throwable e){
            err.println("Fail to exec command: " + toString() + ", cause: " + e.getMessage());
            throw new CommandException(e);
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(getClass().getSimpleName()).append("[ ");
        for(String arg : args){
            builder.append(arg).append(" ");
        }
        for(Iterator<Option> it = cl.iterator(); it.hasNext();){
            Option option = it.next();
            String desc =  "--" + option.getLongOpt() + "=" + option.getValue()+" ";
            builder.append(desc);
        }
        return builder.append("]").toString();
    }

    public static void main(String[] args) throws Exception {
        CreateDataMakeCommand command = new CreateDataMakeCommand("dmaker", "create");
        String[] cmdArgs = {"dmaker", "create", "--conf=conf.xml" , "-t", "local"};
        BaseCommand baseCommand = command.parse(cmdArgs);
        System.out.println(baseCommand.toString());
        baseCommand.exec();
    }
}
