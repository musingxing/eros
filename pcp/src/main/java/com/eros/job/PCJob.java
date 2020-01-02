package com.eros.job;

import com.eros.common.service.Service;
import com.eros.job.conf.JobConfig;
import com.eros.job.service.StandardJobService;
import com.eros.job.task.Consumer;
import com.eros.job.task.Producer;
import com.eros.job.shared.SharedHouse;

/**
 * Producer/Consumer Job
 *
 * @author Eros
 * @since   2020-01-02 17:34
 */
public abstract class PCJob<P> implements Service {

    /**
     * Job service
     */
    private final StandardJobService<P> pcJob;

    /**
     * Constructor
     *
     * @param config   job configuration
     */
    public PCJob(JobConfig config) {
       this.pcJob = new StandardJobService<P>(config);
       for(int i = 0; i < config.getProducerNum(); i++){
           Producer<P> p = buildProducer();
           pcJob.registerProducer(p);
       }
        for(int i = 0; i < config.getConsumerNum(); i++){
            Consumer<P> c = buildConsumer();
            pcJob.registerConsumer(c);
        }
    }

    /**
     * Build producer/consumer task be defined by user
     */
    public abstract Producer<P> buildProducer();
    public abstract Consumer<P> buildConsumer();

    @Override
    public void startup(){
        pcJob.startup();
    }

    public SharedHouse<P> getSharedHouse(){
        return pcJob.getSharedHouse();
    }

    public JobConfig getJobConfig(){
        return pcJob.getJobConfig();
    }

    @Override
    public String serviceName() {
        return pcJob.serviceName();
    }

    @Override
    public void stop(String why) {
        pcJob.stop(why);
    }

    @Override
    public boolean isStopped() {
        return pcJob.isStopped();
    }
}
