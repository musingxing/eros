package com.eros.job.manager;

import com.eros.job.PCJob;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * Job Manager to manage all jobs.
 *
 * @author Eros
 * @since   2020-01-02 15:58
 */
public class JobManager{

    /**
     * Key: Job_Class+Job_Name
     * Value: PCJob Expender
     */
    private static final Map<String, PCJob> REGISTERED_PC_JOBS = new ConcurrentHashMap<>(1024);
    /**
     * Key: Job_Class+Job_Name
     * Value: PCJob Expender
     */
    private static final Map<String, PCJob> RUNNING_PC_JOBS = new ConcurrentHashMap<>(1024);
    /**
     * List for completed jobs
     */
    private static final List<PCJob> COMPLETED_PC_JOBS = new CopyOnWriteArrayList<>();
    /**
     *  Identify as if Job Monitor startup
     */
    private static AtomicBoolean startup = new AtomicBoolean(false);

    /**
     * Register PCJob
     */
    public static void register(PCJob job) {
        if(!startup.get()){
            synchronized (startup){
                if(!startup.get()){
                    JobMonitor jobMonitor = new JobMonitor();
                    jobMonitor.setDaemon(true);
                    jobMonitor.start();
                    new Thread(() -> {
                        jobMonitor.stopMonitor();
                    });
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                            jobMonitor.stopMonitor();
                    }));
                }
            }
        }
        String jobID = job.serviceName();
        if(REGISTERED_PC_JOBS.containsKey(jobID) || RUNNING_PC_JOBS.containsKey(jobID)){
            throw new RuntimeException("Job: " + job.serviceName() + " exist and can't resubmit at the same time");
        }
        REGISTERED_PC_JOBS.put(jobID, job);
    }

    private static String getJobID(String jobTypeName, String jobName){
        return jobTypeName+ "-" + jobName;
    }

    public static PCJob queryJob(String jobTypeName, String jobName){
        String jobID = getJobID(jobTypeName, jobName);
        PCJob job = REGISTERED_PC_JOBS.get(jobID);
        if(job != null)
            return job;
        return RUNNING_PC_JOBS.get(jobID);
    }

    public static List<String> getRegisteredJobIDs(){
        return new ArrayList<>(REGISTERED_PC_JOBS.keySet());
    }

    public static List<PCJob> getRegisteredJobs(){
        return new ArrayList<>(REGISTERED_PC_JOBS.values());
    }

    public static List<String> getRunningJobIDs(){
        return new ArrayList<>(RUNNING_PC_JOBS.keySet());
    }

    public static List<PCJob> getRunningJobs(){
        return new ArrayList<>(RUNNING_PC_JOBS.values());
    }

    private static class JobMonitor extends Thread{
        private static volatile boolean stopped = false;
        @Override
        public void run() {
            while(!stopped){
                // register job checker
                for(Iterator<Map.Entry<String, PCJob>> it = REGISTERED_PC_JOBS.entrySet().iterator(); it.hasNext();){
                    Map.Entry<String, PCJob> entry = it.next();
                    String jobID = entry.getKey();
                    PCJob job = entry.getValue();
                    if(job.isStartup()){
                        it.remove();
                        RUNNING_PC_JOBS.put(jobID, job);
                    }
                }
                // running job checker
                for(Iterator<Map.Entry<String, PCJob>> it = RUNNING_PC_JOBS.entrySet().iterator(); it.hasNext();){
                    Map.Entry<String, PCJob> entry = it.next();
                    PCJob job = entry.getValue();
                    if(job.isStopped()){
                        it.remove();
                        COMPLETED_PC_JOBS.add(job);
                    }
                }
                LockSupport.parkNanos(1*1000*1000*1000L);
            }
        }

        public static void stopMonitor(){
            stopped = true;
            LockSupport.parkNanos(1*1000*1000*1000L);
        }
    }
}
