package com.eros.job.conf;

import java.util.*;

/**
 * Used to save job configuration
 *
 * @author Eros
 * @since   2020-01-02 15:58
 */
public class JobConfig {

    public static final String JOB_NAME_KEY = "job.name";
    public static final String JOB_NAME_DEFAULT = "eros";
    public static final String PC_TIME_OUT_KEY = "job.producer.consumer.timeout";
    public static final long PC_TIME_OUT_DEFAULT = 5*60*1000L; //5 minutes
    public static final int TASKS_NUM_DEFAULT = 4;
    public static final String PRODUCER_NUM_KEY = "job.producer.num";
    public static final int PRODUCER_NUM_DEFAULT = 1;
    public static final String CONSUMER_NUM_KEY = "job.consumer.num";
    public static final int CONSUMER_NUM_DEFAULT = 1;
    public static final String JOB_LOG_ITEM_KEY = "job.item.key.name";
    public static final String JOB_LOG_ITEM_VALUE = "job.item.value.name";
    public static final String JOB_LOG_ITEM_KEY_DEFAULT = "ITEMS";
    public static final String JOB_LOG_ITEM_VALUE_DEFAULT = "VALUES";
    public static final String JOB_LOG_SLEEP_PERIOD_KEY = "job.log.sleep.period";
    public static final long JOB_LOG_SLEEP_PERIOD = 10*1000L;//ms

    /**
     * Save job configuration in kvs
     */
    private final Map<String, Object> kvs = new HashMap<String, Object>(100);

    public String getJobName(){
        Object jobName = kvs.get(JOB_NAME_KEY);
        return jobName == null ? JOB_NAME_DEFAULT : jobName.toString();
    }

    public long getPCTimeOut(){
        Object timeout = kvs.get(PC_TIME_OUT_KEY);
        return timeout == null ? PC_TIME_OUT_DEFAULT : (Long)timeout;
    }

    public int getProducerNum(){
        Object pNum = kvs.get(PRODUCER_NUM_KEY);
        return pNum == null ? PRODUCER_NUM_DEFAULT : (Integer)pNum;
    }

    public int getConsumerNum(){
        Object cNum = kvs.get(CONSUMER_NUM_KEY);
        return cNum == null ? CONSUMER_NUM_DEFAULT : (Integer)cNum;
    }

    public long getLogPrintPeriod(){
        Object period = kvs.get(JOB_LOG_SLEEP_PERIOD_KEY);
        return period == null ? JOB_LOG_SLEEP_PERIOD : (Long)period;
    }

    public Object put(String key, Object value) {
        return kvs.put(key, value);
    }

    public Object get(String key){
        return kvs.get(key);
    }

    public Object get(String key, Object defaultV){
        return kvs.get(key) == null ? defaultV : kvs.get(key);
    }

    public Object remove(Object key) {
        return kvs.remove(key.toString());
    }

    public void putAll(Map<? extends String, ?> m) {
        kvs.putAll(m);
    }

    public void clear() {
        kvs.clear();
    }

    public Set<String> keySet() {
        return kvs.keySet();
    }

    public Collection<Object> values() {
        return kvs.values();
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
