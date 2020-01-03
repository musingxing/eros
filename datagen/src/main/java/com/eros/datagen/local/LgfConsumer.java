package com.eros.datagen.local;

import com.eros.job.exception.PCException;
import com.eros.job.task.Consumer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;

/**
 * Consumer Implement to consume products in continuous mode.
 *
 * @author Eros
 * @since 2020-01-03 17:10
 */
public class LgfConsumer extends Consumer<List<String>> {

    private static LongAdder GLOBAL_COUNT = new LongAdder();
    /**
     * Local generate file configuration
     */
    private final LgfJobConfig config;
    /**
     * File Manager
     */
    private final LocalFileManager fileManager;
    /**
     * Current file writer
     */
    private BufferedWriter writer;
    private int lineNumCounter = 0;
    /**
     * File line args
     */
    private final int lineMaxNum;

    /**
     * Constructor
     *
     * @param taskName task name
     * @param job      job
     */
    public LgfConsumer(String taskName, LgfPCJob job) {
        super(taskName, job);
        this.fileManager = job.getFileManager();
        this.config = (LgfJobConfig) job.getJobConfig();
        this.lineMaxNum = config.getFileLineMaxNum();
    }

    @Override
    public void consume(List<String> lines) throws PCException {

        if (lines == null || lines.isEmpty()) {
            return;
        }

        try {
            // init write file
            if (writer == null) {
                String fileName = fileManager.nextFileName();
                if (fileName == null)
                    throw new RuntimeException("Not found next file, " + fileManager);
                File file = new File(fileName);
                this.writer = new BufferedWriter(new FileWriter(file));
            }

            // flush data
            for (String line : lines) {
                writer.write(line + "\n");
                GLOBAL_COUNT.increment();
            }
            writer.flush();

            // reset file
            lineNumCounter += lines.size();

            if (lineNumCounter >= lineMaxNum) {
                writer.close();
                writer = null;
                lineNumCounter = 0;
            }
        } catch (IOException e) {
            throw new PCException(e);
        }
    }

    @Override
    public void after() {
        try {
            if (writer != null)
                writer.close();
        } catch (IOException e) {
        }
    }

    public static long produceCount(){
        return GLOBAL_COUNT.longValue();
    }
}
