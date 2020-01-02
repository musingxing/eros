package com.eros.job.conf;

import org.junit.Assert;
import org.junit.Test;

public class JobConfigTest {

    private final JobConfig jobConfig = new JobConfig();

    @Test
    public void test_1_Int(){
        String key = "job.max.count.default";
        int num = 16;
        jobConfig.put(key, num);
        Assert.assertEquals(num, jobConfig.get(key));
    }

    @Test
    public void test_2_Str(){
        String key = "job.name.default";
        String name = "job";
        Assert.assertEquals(name, jobConfig.get(key));
        Assert.assertEquals(name, jobConfig.getJobName());
    }
}
