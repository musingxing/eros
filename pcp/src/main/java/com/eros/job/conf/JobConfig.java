package com.eros.job.conf;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Used to save job configuration
 *
 * @author Eros
 * @since   2020-01-02 15:58
 */
public class JobConfig {

    public static final JobConfKey JOB_NAME = new JobConfKey("job.name", "job name", "eros");
    public static final JobConfKey PC_TIME_OUT = new JobConfKey("job.producer.consumer.timeout", "producer/consumer task timeout", 5*60*1000L);
    public static final JobConfKey PRODUCER_NUM = new JobConfKey("job.producer.num", "the number of producer task", 1);
    public static final JobConfKey CONSUMER_NUM = new JobConfKey("job.consumer.num", "the number of consumer task", 1);
    public static final JobConfKey JOB_LOG_ITEM_KEY = new JobConfKey("job.item.key.name", "the head key item", "ITEMS");
    public static final JobConfKey JOB_LOG_ITEM_VALUE = new JobConfKey("job.item.value.name", "the head value item", "VALUES");
    public static final JobConfKey JOB_LOG_SLEEP_PERIOD =  new JobConfKey("job.log.sleep.period", "the period to print log", 10*1000L);
    public static final ThreadLocal<SimpleDateFormat> DATE_FORMATTER = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        }
    };
    public static final JobConfKey JOB_DATE_FORMATTER = new JobConfKey("job.date.formatter", "format the time", DATE_FORMATTER.get());

    /**
     * Save job configuration in kvs
     */
    private final Map<JobConfKey, Object> kvs = new LinkedHashMap<JobConfKey, Object>(100);

    public static class JobConfKey{
        public String KEY;
        public String DESC;
        public Object DEFAULT;

        public JobConfKey(String KEY, String DESC, Object DEFAULT) {
            this.KEY = KEY;
            this.DESC = DESC;
            this.DEFAULT = DEFAULT;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            JobConfKey that = (JobConfKey) o;
            return Objects.equals(KEY, that.KEY);
        }

        @Override
        public int hashCode() {
            return Objects.hash(KEY);
        }

        @Override
        public String toString() {
            return "[" + DESC + "] [default:" + DEFAULT + "] [" + KEY + "]";
        }
    }

    public SimpleDateFormat getDefaultDateFormatter(){
        return DATE_FORMATTER.get();
    }

    public String getHeadItemKey(){
        Object itemKey = kvs.get(JOB_LOG_ITEM_KEY);
        return itemKey == null ? (String)JOB_LOG_ITEM_KEY.DEFAULT : itemKey.toString();
    }

    public String getHeadItemValue(){
        Object itemValue = kvs.get(JOB_LOG_ITEM_VALUE);
        return itemValue == null ? (String)JOB_LOG_ITEM_VALUE.DEFAULT : itemValue.toString();
    }

    public String getJobName(){
        Object jobName = kvs.get(JOB_NAME);
        return jobName == null ? (String)JOB_NAME.DEFAULT : jobName.toString();
    }

    public long getPCTimeOut(){
        Object timeout = kvs.get(PC_TIME_OUT);
        return timeout == null ? (long)PC_TIME_OUT.DEFAULT : (Long)timeout;
    }

    public int getProducerNum(){
        Object pNum = kvs.get(PRODUCER_NUM);
        return pNum == null ? (int)PRODUCER_NUM.DEFAULT : (Integer)pNum;
    }

    public int getConsumerNum(){
        Object cNum = kvs.get(CONSUMER_NUM);
        return cNum == null ? (int)CONSUMER_NUM.DEFAULT : (Integer)cNum;
    }

    public long getLogPrintPeriod(){
        Object period = kvs.get(JOB_LOG_SLEEP_PERIOD);
        return period == null ? (long)JOB_LOG_SLEEP_PERIOD.DEFAULT : (Long)period;
    }

    public Object put(JobConfKey key, Object value) {
        return kvs.put(key, value);
    }

    public Object get(JobConfKey key){
        return kvs.get(key);
    }

    public Object get(JobConfKey key, Object defaultV){
        return kvs.get(key) == null ? defaultV : kvs.get(key);
    }

    public Object remove(JobConfKey key) {
        return kvs.remove(key.toString());
    }

    public void putAll(Map<? extends JobConfKey, ?> m) {
        kvs.putAll(m);
    }

    public void clear() {
        kvs.clear();
    }

    public Set<JobConfKey> keySet() {
        return kvs.keySet();
    }

    public Collection<Object> values() {
        return kvs.values();
    }

    public Map<JobConfKey, Object> copyAll(){
        Map<JobConfKey, Object> newKVs = new LinkedHashMap<JobConfKey, Object>(100);
        newKVs.put(new JobConfKey("Job Configuration Class", "the implement class of job config", JobConfig.class.getCanonicalName()),
                getClass().getCanonicalName());
        newKVs.put(JOB_NAME, getJobName());
        newKVs.put(PRODUCER_NUM, getProducerNum());
        newKVs.put(CONSUMER_NUM, getConsumerNum());
        newKVs.put(PC_TIME_OUT, getPCTimeOut());
        newKVs.put(JOB_LOG_SLEEP_PERIOD, getLogPrintPeriod()+"\n");

        newKVs.putAll(kvs);
        return newKVs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobConfig that = (JobConfig) o;
        return kvs.equals(that.kvs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kvs);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+kvs.toString();
    }
}
