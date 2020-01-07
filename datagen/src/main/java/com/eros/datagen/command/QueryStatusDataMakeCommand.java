package com.eros.datagen.command;

import com.eros.common.util.LoggerUtil;
import com.eros.datagen.local.LgfJobConfig;
import com.eros.datagen.local.LgfPCJob;
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
public class QueryStatusDataMakeCommand extends BaseCommand {

    private static final Logger logger = LoggerUtil.getLogger("command", QueryStatusDataMakeCommand.class);
    private static Options options = new Options();
    static {
        options.addOption(new Option("c", true, "configuration"));
        options.addOption(new Option("t", true, "type"));
        options.addOption(new Option("p", false, "print result"));
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
    public QueryStatusDataMakeCommand(String cmdStr, String optionStr) {
        super("dmaker status", "[-t type] [-c conf]");
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
        String jobType = "local";
        boolean hasType = cl.hasOption("t");
        if(hasType){
            jobType = cl.getOptionValue("t");
            if(!jobTypes.contains(jobType))
                throw new MalformedCommandException("-t argument must be job type in " + jobTypes);
        }

        boolean hasConf = options.hasOption("c");
        Map<JobConfig.JobConfKey, Object> confKVs = new LinkedHashMap<>();
        if(hasConf){
            String conf = cl.getOptionValue("c");
            File confFile = new File(conf);
            if(!conf.endsWith(".xml"))
                throw new MalformedCommandException("-c argument specify configuration file " + conf + " is not '.xml'");
            if(!confFile.exists())
                throw new MalformedCommandException("-c argument specify configuration file "  + conf + " inexistence");
            if(!confFile.isDirectory())
                throw new MalformedCommandException("-c argument specify configuration file " + conf + " is a directory");

            // load config items
        }

        switch (jobType){
            case "local":{
                LgfJobConfig config = new LgfJobConfig();
                // add items from config file
                if(!confKVs.isEmpty())
                    config.putAll(confKVs);
                LgfPCJob job = new LgfPCJob(config);
                job.startup();
                // Join monitor manager for job

                // print job info
                out.println(job.getJobConfig());
                break;

            }
            case "hdfs":{

            }

            default:{

            }
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
        QueryStatusDataMakeCommand command = new QueryStatusDataMakeCommand("dmaker", "create");
        String[] cmdArgs = {"dmaker", "create", "-c" , "conf.xml", "-t", "local"};
        BaseCommand baseCommand = command.parse(cmdArgs);
        System.out.println(baseCommand.toString());
        baseCommand.exec();
    }
}
