package com.eros.datagen.local;

import com.eros.datagen.generator.DataGenerator;
import com.eros.job.conf.JobConfig;

import java.util.*;

/**
 * Used to extends job configuration
 *
 * @author Eros
 * @since   2020-01-03 17:10
 */
public class LgfJobConfig extends JobConfig {

    /** ***************************************  File Directory Concurrent Mode ******************************** */
//    public static final String JOB_FILE_GENERATOR_PARALLEL_MODE_KEY = "job.file.generator.parallel.mode";
//    public static final ConcurrentMode JOB_FILE_GENERATOR_PARALLEL_MODE_DEFAULT = ConcurrentMode.Dir_Parallel;

    /** *************************************** File Information *********************************************** */
    public static final JobConfKey JOB_FILE_DIRS = new JobConfKey("job.file.dirs", "the data file directories", null);
    public static final JobConfKey JOB_FILE_NAME_PREFIX  = new JobConfKey("job.file.name.prefix", "the prefix of file name", "data_file");
    public static final JobConfKey JOB_FILE_TYPE = new JobConfKey("job.file.type", "the file type", ".csv");

    /** ***************************************  File Line Information *********************************************** */
    public static final JobConfKey JOB_FILE_TOTAL_LINE_NUM = new JobConfKey("job.file.total.line.num", "the max line number for all files", 1000_0000L);
    public static final JobConfKey JOB_FILE_LINE_MAX_NUM = new JobConfKey("job.file.line.max.number", "the max number of file lines", 100_0000);
    public static final JobConfKey JOB_FILE_FIELD_SEQ  = new JobConfKey("job.file.field.separator", "the line separator between fields", '\t');
    public static final JobConfKey JOB_FILE_LINE_BATCH_FLUSH = new JobConfKey("job.file.line.batch.flush", "the batch size of lines to flush", 1000);

    /** *************************************** Data Generators *********************************************** */
    public static final JobConfKey JOB_FILE_DATA_GENERATOR = new JobConfKey("job.file.data.generator", "the field generator in lines", null);

//    enum ConcurrentMode{
//        Dir_In_Order,
//        Dir_Parallel
//    }

    public List<DataGenerator> getDataGenerators(){
        // convert
        Object value = get(JOB_FILE_DATA_GENERATOR);
        if(value == null)
            return null;
        if( value instanceof List)
            return (List<DataGenerator>)get(JOB_FILE_DATA_GENERATOR);
        if(value instanceof String){
            List<DataGenerator> generators = new ArrayList<>();
            String[] genStrs = ((String)value).split(",");
            for(String str : genStrs){
                DataGenerator dataGenerator = DataGenerator.parse(str.trim());
                generators.add(dataGenerator);
            }
            put(JOB_FILE_DATA_GENERATOR, generators);
            return generators;
        }
        throw new IllegalArgumentException("Unknown data generator: " + value);
    }

    public LgfJobConfig setDataGenerators(List<DataGenerator> generators){
        put(JOB_FILE_DATA_GENERATOR, generators);
        return this;
    }

    public List<String> getFileDirs(){ //需要转换
        Object dirs = get(JOB_FILE_DIRS);
        if(dirs == null){
           return null;
        }
        if(dirs instanceof String){
            List<String> curDirs = new ArrayList<>();
            String[] genDirs = ((String)dirs).split(",");
            for(String genDir : genDirs){
                curDirs.add(genDir.trim());
            }
            put(JOB_FILE_DIRS, curDirs);
            return curDirs;
        }

        if(! (dirs instanceof Collection)){
            throw new IllegalArgumentException("Data directory is not list '" + JOB_FILE_DIRS + "'.");
        }
        Set<String> curDirs = new HashSet<>();
        Collection elems = (Collection) dirs;
        for(Object elem : elems){
            curDirs.add(elem.toString());
        }
        return new ArrayList<>(curDirs);
    }

    public LgfJobConfig setFileDirs(List<String> dirs){
        if(dirs != null){
            put(JOB_FILE_DIRS, dirs);
        }
        return this;
    }

    public long getFileTotalLineMaxNum(){
        return (long) get(JOB_FILE_TOTAL_LINE_NUM, JOB_FILE_TOTAL_LINE_NUM.DEFAULT);
    }

    public LgfJobConfig setFileTotalLineMaxNum(long maxNum){
        if(maxNum < 1L){
            throw new IllegalArgumentException("Max line num can't be less than 1, current: " + maxNum);
        }
        put(JOB_FILE_TOTAL_LINE_NUM, maxNum);
        return this;
    }

    public int getFileLineMaxNum(){
        return (int) get(JOB_FILE_LINE_MAX_NUM, JOB_FILE_LINE_MAX_NUM.DEFAULT);
    }

    public LgfJobConfig setFileLineMaxNum(int maxNum){
        if(maxNum < 1){
            throw new IllegalArgumentException("Max line num can't be less than 1, current: " + maxNum);
        }
        put(JOB_FILE_LINE_MAX_NUM, maxNum);
        return this;
    }

    public String getDataFilePrefix(){
        return (String) get(JOB_FILE_NAME_PREFIX, JOB_FILE_NAME_PREFIX.DEFAULT);
    }

    public LgfJobConfig setDataFilePrefix(String filePrefix){
        put(JOB_FILE_NAME_PREFIX, filePrefix);
        return this;
    }

    public String getDataFileType(){
        return (String) get(JOB_FILE_TYPE, JOB_FILE_TYPE.DEFAULT);
    }

    public LgfJobConfig setDataFileType(String type){
        put(JOB_FILE_TYPE, type);
        return this;
    }

    public char getDataFileFieldSeparator(){
        return (char) get(JOB_FILE_FIELD_SEQ, JOB_FILE_FIELD_SEQ.DEFAULT);
    }

    public LgfJobConfig setDataFileFieldSeparator(char seq){
        put(JOB_FILE_FIELD_SEQ, seq);
        return this;
    }

    public int getFileLineBatchSize(){
        return (int) get(JOB_FILE_LINE_BATCH_FLUSH, JOB_FILE_LINE_BATCH_FLUSH.DEFAULT);
    }

    public LgfJobConfig setFileLineBatchSize(int batch){
        if(batch < 1)
            throw new IllegalArgumentException("Batch size can't be less than 1. current: " + batch);
        put(JOB_FILE_LINE_BATCH_FLUSH, batch);
        return this;
    }

    @Override
    public Map<JobConfKey, Object> copyAll() {
        Map<JobConfKey, Object> kvs = super.copyAll();
        kvs.put(LgfJobConfig.JOB_FILE_DIRS, getFileDirs());
        kvs.put(LgfJobConfig.JOB_FILE_NAME_PREFIX, getDataFilePrefix());
        kvs.put(LgfJobConfig.JOB_FILE_TYPE, getDataFileType());
        kvs.put(LgfJobConfig.JOB_FILE_NAME_PREFIX, getDataFilePrefix()+"\n");

        kvs.put(LgfJobConfig.JOB_FILE_TOTAL_LINE_NUM, getFileTotalLineMaxNum());
        kvs.put(LgfJobConfig.JOB_FILE_LINE_MAX_NUM, getFileLineMaxNum());
        kvs.put(LgfJobConfig.JOB_FILE_FIELD_SEQ, "'"+getDataFileFieldSeparator()+"'");
        kvs.put(LgfJobConfig.JOB_FILE_TYPE, getDataFileType());
        kvs.put(LgfJobConfig.JOB_FILE_LINE_BATCH_FLUSH, getFileLineBatchSize());
        kvs.put(LgfJobConfig.JOB_FILE_DATA_GENERATOR, getDataGenerators());

        return kvs;
    }

    //暂未实现
    public void check(){

    }
}
