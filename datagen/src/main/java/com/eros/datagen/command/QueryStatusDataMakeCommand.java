package com.eros.datagen.command;

import com.eros.job.PCJob;
import com.eros.job.conf.JobConfig;
import com.eros.job.manager.JobManager;
import com.eros.shell.command.BaseCommand;
import com.eros.shell.exception.CommandException;
import com.eros.shell.exception.CommandParseException;
import org.apache.commons.cli.*;

import java.util.List;

/**
 * Command for Creating Data Maker.
 *
 * dmaker [OPTIONS...] {COMMAND} ...
 *
 * dmaker status [JOB] {JOB_NAME}
 * e.g.
 *    dmaker status --type=local --job-name=eros-first-job
 *
 *
 * @author Eros
 * @since   2020-01-06 10:42
 */
public class QueryStatusDataMakeCommand extends BaseCommand {

    private static Options options = new Options();
    static {
        options.addOption(new Option("a", "all",false, "all job"));
        options.addOption(new Option("t", "type",true, "job type"));
        options.addOption(new Option("j", "job-name",true, "job name"));
    }

    private String[] args;
    private CommandLine cl;

    /**
     * Constructor
     */
    public QueryStatusDataMakeCommand() {
        super("dmaker", "status");
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

        if(cl.hasOption("a")){
            List<String> registeredJobs = JobManager.getRegisteredJobIDs();
            if(registeredJobs != null && !registeredJobs.isEmpty()){
                System.out.println("Registered Jobs: ");
                for(String job : registeredJobs){
                    System.out.println("\t" + job);
                }
            }

            List<String> runningJobs = JobManager.getRunningJobIDs();
            if(runningJobs != null && !runningJobs.isEmpty()){
                System.out.println("RunningJobs Jobs: ");
                for(String job : runningJobs){
                    System.out.println("\t" + job);
                }
            }
            return true;
        }

        if(!cl.hasOption("j"))
            System.out.println("\nWARNING: Not set job name and use default: " + JobConfig.JOB_NAME.defaultValue() + "\n");
        String jobName = cl.hasOption("j") ? cl.getOptionValue("j") : (String)JobConfig.JOB_NAME.defaultValue();
        String jobType = cl.hasOption("t") ? cl.getOptionValue("t") : (String)JobConfig.JOB_TYPE_NAME.defaultValue();
        PCJob job = JobManager.queryJob(jobType, jobName);
        if(job == null)
            System.out.println(String.format("Not found job<type=%s, name=%s>", jobType, jobName));
        else
            System.out.println(String.format("Job<type=%s, name=%s>\n%s", jobType, jobName, job.getJobInfo()));

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
        QueryStatusDataMakeCommand command = new QueryStatusDataMakeCommand();
        String[] cmdArgs = {"dmaker", "status"};
        command.parse(cmdArgs);
        System.out.println(command.toString());
        System.out.println(command.getUsageStr());
        command.exec();
    }
}
