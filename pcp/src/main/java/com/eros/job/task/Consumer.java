package com.eros.job.task;

import com.eros.common.util.LoggerUtil;
import com.eros.common.service.Stoppable;
import com.eros.job.PCJob;
import com.eros.job.exception.PCException;
import com.eros.job.shared.SharedHouse;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Consumer to consume products in continuous mode.
 *
 * <P> product
 * @author Eros
 * @since   2020-01-02 15:58
 */
public abstract class Consumer<P> implements Runnable, Stoppable {

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
    protected final Logger logger;

    /**
     * Constructor
     *
     * @param taskName  task name
     * @param job       pc mode job
     */
    public Consumer(String taskName, PCJob<P> job) {
        this.taskName = taskName;
        this.sharedHouse = job.getSharedHouse();
        this.logger = LoggerUtil.getLogger(job.serviceName()+"-consumer", this.getClass());
    }

    @Override
    public void run() {

        try{
            // before action
            before();
            if (logger.isLoggable(Level.INFO))
                logger.info(String.format("task:%s starting...", taskName));

            while (!stopped) {
                try {
                    P p = sharedHouse.poll();
                    if(p != null){
                        consume(p);
                        continue;
                    }

                    if(sharedHouse.isProducerStopped())
                        break;
                }
                catch (PCException e) {
                    logger.log(Level.WARNING, String.format("consume-fail,task:%s continue,cause: ", taskName), e);
                }
                catch (Throwable e){
                    logger.log(Level.SEVERE, String.format("consume-unknown error,task:%s stop,cause: ", taskName), e);
                    break;
                }
            }

            // after action
            after();
            if (logger.isLoggable(Level.INFO))
                logger.info(String.format("task:%s ending...", taskName));
        }
        finally {
            this.stopped = true;
        }
    }

    /**
     * User to define how to consume product and what product should be do
     *
     * @return <P> product
     */
    public abstract void consume(P p) throws PCException;

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
