package com.eros.local;

import com.eros.common.string.DoubleColumnTable;
import com.eros.common.util.LoggerUtil;
import com.eros.datagen.generator.DataGenerator;
import com.eros.datagen.generator.impl.RandomHexCharsGenerator;
import com.eros.datagen.generator.impl.RandomIntGenerator;
import com.eros.datagen.local.LgfJobConfig;
import com.eros.job.conf.JobConfig;
import org.apache.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LgfJobConfigTest {

    private static final Logger logger = LoggerUtil.getLogger("test", LgfJobConfigTest.class);
    private final LgfJobConfig jobConfig = new LgfJobConfig();

    @Test
    public void test_1_ToTable(){
        DoubleColumnTable table = DoubleColumnTable.newTable("KEYS", "VALUES");
        Map<JobConfig.JobConfKey, Object> confKVs = jobConfig.copyAll();
        String str = table.format(confKVs);
        logger.info(str);
    }

    @Test
    public void test_2_Set_Table(){
        DoubleColumnTable table = DoubleColumnTable.newTable("KEYS", "VALUES");

        List<DataGenerator> generators = new ArrayList<>();
        generators.add(new RandomIntGenerator());
        generators.add(new RandomHexCharsGenerator());
        generators.add(new RandomHexCharsGenerator());
        generators.add(new RandomHexCharsGenerator());
        jobConfig.put(LgfJobConfig.JOB_FILE_DATA_GENERATOR, generators);

        List<String> dirs = new ArrayList<>();
        dirs.add(File.separator + "data1" + File.separator + "files");
        dirs.add(File.separator + "data2" + File.separator + "files");
        dirs.add(File.separator + "data3" + File.separator + "files");
        dirs.add(File.separator + "data4" + File.separator + "files");
        jobConfig.put(LgfJobConfig.JOB_FILE_DIRS, dirs);

        Map<JobConfig.JobConfKey, Object> confKVs = jobConfig.copyAll();
        String str = table.format(confKVs);
        logger.info(str);
    }
}
