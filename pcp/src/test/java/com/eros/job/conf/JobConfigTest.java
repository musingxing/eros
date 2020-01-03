package com.eros.job.conf;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JobConfigTest {

    private final JobConfig jobConfig = new JobConfig();

    @Test
    public void test_1_Int(){
        JobConfig.JobConfKey key = new JobConfig.JobConfKey("job.max.count.default", "-", 10);
        int num = 16;
        jobConfig.put(key, num);
        Assert.assertEquals(num, jobConfig.get(key));
    }

    @Test
    public void test_2_Str(){
        JobConfig.JobConfKey key = new JobConfig.JobConfKey("job.name", "-", "eros");
        String name = "job";
        jobConfig.put(key, name);
        Assert.assertEquals(name, jobConfig.get(key));
        Assert.assertEquals(name, jobConfig.getJobName());
    }
}
