package com.eros.datagen.local;

import com.eros.datagen.generator.DataGenerator;
import com.eros.job.PCJob;
import com.eros.job.task.Producer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;

/**
 * Producer implement to produce products in continuous mode.
 *
 * @author Eros
 * @since 2020-01-03 17:10
 */
public class LgfProducer extends Producer<List<String>> {

    private static LongAdder GLOBAL_COUNT = new LongAdder();
    /**
     * Local generate file configuration
     */
    private final LgfJobConfig config;
    /**
     * Data generator set
     */
    private final List<DataGenerator> dataGenerators;
    /**
     * Batch size
     */
    private final int batch;
    /**
     * Max lines
     */
    private final long totalNum;
    /**
     * Field Separator
     */
    private final char sep;

    /**
     * Constructor
     *
     * @param taskName task name
     * @param job      job
     */
    public LgfProducer(String taskName, PCJob<List<String>> job) {
        super(taskName, job);
        this.config = (LgfJobConfig) job.getJobConfig();
        this.batch = config.getFileLineBatchSize();
        this.totalNum = config.getFileTotalLineMaxNum();
        this.sep = config.getDataFileFieldSeparator();
        this.dataGenerators = config.getDataGenerators();
    }

    @Override
    public List<String> produce() {

        if (GLOBAL_COUNT.longValue() >= totalNum)
            return null;

        List<String> lines = new ArrayList<>(batch);
        for (int i = 0; i < batch; i++) {
            StringBuilder line = new StringBuilder();
            for (int j = 0, size = dataGenerators.size(); j < size; j++) {
                DataGenerator generator = dataGenerators.get(j);
                line.append(generator.generate());
                if (j < (size - 1))
                    line.append(sep);
            }

            if (GLOBAL_COUNT.longValue() >= totalNum)
                break;
            lines.add(line.toString());
            GLOBAL_COUNT.increment();
        }
        return lines;
    }

    public static long produceCount(){
        return GLOBAL_COUNT.longValue();
    }
}
