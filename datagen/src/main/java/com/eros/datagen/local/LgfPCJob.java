package com.eros.datagen.local;

import com.eros.datagen.generator.DataGenerator;
import com.eros.datagen.generator.impl.RandomHexCharsGenerator;
import com.eros.datagen.generator.impl.RandomIntGenerator;
import com.eros.job.PCJob;
import com.eros.job.conf.JobConfig;
import com.eros.job.task.Consumer;
import com.eros.job.task.Producer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Consumer to consume products in continuous mode.
 *
 * @author Eros
 * @since 2020-01-03 17:10
 */
public class LgfPCJob extends PCJob<List<String>> {

    /**
     * Task order
     */
    private int produceTaskOrder = 0;
    private int consumerTaskOrder = 0;
    private final LocalFileManager fileManager;

    /**
     * Constructor
     *
     * @param config job configuration
     */
    public LgfPCJob(LgfJobConfig config) {
        super(config);
        this.fileManager = buildFileManager(config);
    }

    private LocalFileManager buildFileManager(LgfJobConfig config) {
        List<String> dirs = config.getFileDirs();
        return LocalFileManager.newFileManager(dirs, config.getDataFilePrefix(), config.getDataFileType());
    }

    @Override
    public Producer<List<String>> buildProducer() {
        String taskName = getJobConfig().getJobName() + "-producer-" + (produceTaskOrder++);
        return new LgfProducer(taskName, this);
    }

    @Override
    public Consumer<List<String>> buildConsumer() {
        String taskName = getJobConfig().getJobName() + "-consumer-" + (consumerTaskOrder++);
        return new LgfConsumer(taskName, this);
    }

    @Override
    public void before() {
        Map<JobConfig.JobConfKey, Object> kvs = ((LgfJobConfig) getJobConfig()).copyAll();
        Map<Object, Object> logKVs = new LinkedHashMap<>();
        logKVs.putAll(kvs);
        Map<String, String> appendKVs = appendShowMSG();
        if (appendKVs != null && !appendKVs.isEmpty()) {
            logKVs.putAll(appendKVs);
        }
        logger.info(table.format(logKVs));
    }

    @Override
    public Map<String, String> appendShowMSG() {
        Map<String, String> kvs = new LinkedHashMap<>();
        kvs.put("Produced number", Long.toString(LgfProducer.produceCount()));
        kvs.put("Consumed number", Long.toString(LgfProducer.produceCount()));
        kvs.put("Used file count", Integer.toString(fileManager.getUsedFileNames().size()));

        return kvs;
    }

    public LocalFileManager getFileManager() {
        return fileManager;
    }

    /**
     * Build Lgf-PCJob
     *
     * @param jobType   job type
     * @param jobName   job name
     * @param confFile conf file
     * @return {@link LgfPCJob}
     */
    public static LgfPCJob buildJob(String jobType, String jobName, String confFile){

        LgfJobConfig config = new LgfJobConfig();
        // add items from config file
        Map<JobConfig.JobConfKey, Object> confKVs = (confFile==null || confFile.isEmpty())? null : LgfJobConfig.parseXMLConf(confFile);
        if(confKVs != null && !confKVs.isEmpty()) {
            config.putAll(confKVs);
        }
        if(jobName != null)
            config.setJobName(jobName);
        if(jobType != null)
            config.setJobTypeName(jobType);

        LgfPCJob job = new LgfPCJob(config);
        return job;
    }

    // JUST FOR TEST
    public static void main(String[] args) {
        LgfJobConfig config = new LgfJobConfig();
        config.put(JobConfig.PRODUCER_NUM, 10);
        config.put(JobConfig.CONSUMER_NUM, 10);
        config.put(LgfJobConfig.JOB_FILE_TOTAL_LINE_NUM, 1_0000_0000L);
        List<DataGenerator> generators = new ArrayList<>();
        generators.add(new RandomIntGenerator());
        generators.add(new RandomHexCharsGenerator());
        generators.add(new RandomHexCharsGenerator());
        config.put(LgfJobConfig.JOB_FILE_DATA_GENERATOR, generators);

        List<String> dirs = new ArrayList<>();
        dirs.add("D:\\data1");
        dirs.add("D:\\data2");
        dirs.add("D:\\data3");
        config.put(LgfJobConfig.JOB_FILE_DIRS, dirs);

        LgfPCJob job = new LgfPCJob(config);
        job.startup();
    }
}
