package com.eros.job.task;

import com.eros.job.PCJob;
import com.eros.job.conf.JobConfig;
import com.eros.job.exception.PCException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TestPCJob extends PCJob<String> {

    public TestPCJob(JobConfig config) {
        super(config);
    }

    @Override
    public Producer<String> buildProducer() {
        return new TestProducer(this);
    }

    @Override
    public Consumer<String> buildConsumer() {
        return new TestConsumer(this);
    }

    private static String generateTaskName(JobConfig config, int order, String taskType){
        return new StringBuilder(config.getJobName())
                .append("-").append(taskType.toLowerCase())
                .append("-").append(order).toString();
    }

    private static class TestProducer extends Producer<String> {
        private static AtomicLong ORDER = new AtomicLong(0L);
        private final Long maxCount;

        public TestProducer(PCJob<String> job) {
            super(generateTaskName(job.getJobConfig(), (int)ORDER.incrementAndGet(), "p"), job);
            Object o = job.getJobConfig().get(new JobConfig.JobConfKey("job.max.count", "the max line number for job", 10_0000L));
            if(o == null)
                maxCount = 10_0000L;
            else
                maxCount = (Long)o;
        }

        @Override
        public String produce() throws PCException{
            long order = ORDER.incrementAndGet();
            if(order > maxCount)
                return null;
            return "line:" + order + "-" + taskName();
        }
    }

    private static class TestConsumer extends Consumer<String> {

        private static AtomicInteger TASK_ORDER = new AtomicInteger(0);
        private BufferedWriter writer;

        public TestConsumer(PCJob<String> job) {
            super(generateTaskName(job.getJobConfig(), TASK_ORDER.incrementAndGet(), "c"), job);
        }

        @Override
        public void consume(String str) throws PCException {

            try{
                if(writer == null){
                    File file = new File(taskName());
                    this.writer = new BufferedWriter(new FileWriter(file));
                }
                writer.write(str+"\n");
                writer.flush();
            }catch (Throwable e){
                logger.warn("consume,fail,cause: " + e.getMessage());
            }
        }

        @Override
        public void before() {
        }

        @Override
        public void after() {
            try{
                if (logger.isInfoEnabled())
                    logger.info(String.format("task:%s ending...", taskName()));
                if (writer == null)
                    return;
                writer.close();
            }catch (Throwable e){
                logger.error("fail,close file output stream", e);
            }
        }
    }

    public static void main(String[] args) {

        JobConfig config = new JobConfig();
        config.put(JobConfig.CONSUMER_NUM, 2);
        config.put(JobConfig.PRODUCER_NUM, 2);
        TestPCJob pcJob = new TestPCJob(config);
        pcJob.startup();
    }
}
