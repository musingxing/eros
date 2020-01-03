package com.eros.job.task;

import com.eros.job.PCJob;
import com.eros.job.conf.JobConfig;
import com.eros.common.util.LoggerUtil;
import com.eros.common.service.Stoppable;
import com.eros.job.exception.PCException;
import com.eros.job.shared.SharedHouse;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * Producer to produce products in continuous mode.
 *
 * <P> product
 * @author Eros
 * @since   2020-01-02 15:58
 */
public abstract class Producer<P> implements Runnable, Stoppable {

    /**
     * Identify as task if stopped
     */
    private volatile boolean stopped;
    /**
     * Task name
     */
    private final String taskName;
    /**
     * StoreHouse for products
     */
    private final SharedHouse<P> sharedHouse;
    /**
     * Task/Job configuration
     */
    private final JobConfig config;
    protected final Logger logger;

    /**
     * Constructor
     *
     * @param taskName  task name
     * @param job       pc mode job
     */
    public Producer(String taskName, PCJob<P> job) {
        this.taskName = taskName;
        this.sharedHouse = job.getSharedHouse();
        this.config = job.getJobConfig();
        this.logger = LoggerUtil.getLogger(job.serviceName()+"-producer", this.getClass());
    }

    @Override
    public void run() {
        try{
            // before action
            before();
            if (logger.isInfoEnabled())
                logger.info(String.format("task:%s starting...", taskName));

            while (!stopped && !sharedHouse.isConsumerStopped()) {
                try {
                    P p = produce();
                    if (p == null)
                        break;

                    long escapedTime = 0L;
                    boolean result = false;
                    while(escapedTime < config.getPCTimeOut()){
                        result = sharedHouse.offer(p, 100L, TimeUnit.MILLISECONDS);
                        if(result)
                            break;
                        if(sharedHouse.isConsumerStopped())
                            break;
                    }
                    if (!result) {
                        logger.warn(String.format("produce,offer,fail,task:%s,product:%s", taskName, p));
                    }
                }
                catch (PCException e) {
                    logger.warn(String.format("produce-fail,task:%s continue,cause: ", taskName), e);
                }
                catch (Throwable e){
                    logger.error(String.format("produce-fail,task:%s stop,cause: ", taskName), e);
                    break;
                }
            }

            // after action
            after();
            if (logger.isInfoEnabled())
                logger.info(String.format("task:%s ending...", taskName));
        } finally {
            this.stopped = true;
        }
    }

    /**
     * User to define how to produce product and what product should be do
     *
     * @return <P> product
     */
    public abstract P produce() throws PCException;

    public String taskName(){
        return taskName;
    }

    public void before() {
    }

    public void after() {
    }

    public boolean isStopped() {
        return stopped;
    }

    @Override
    public void stop(String why) {
        if(stopped) return;
        logger.info("Stopping task: " + taskName + ", cause: " + why);
        this.stopped = true;
    }

    @Override
    public String toString() {
        return taskName;
    }
}
