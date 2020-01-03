package com.eros.common.string;

import com.eros.common.util.LoggerUtil;
import org.apache.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.LinkedHashMap;
import java.util.Map;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DoubleColumnTableTest {

    private static final String LOG_FILE_NAME = "test";
    private static final Logger logger = LoggerUtil.getLogger(LOG_FILE_NAME, DoubleColumnTableTest.class);

    @Test
    public void printTable_1_SetArgs(){
        DoubleColumnTable table = DoubleColumnTable.newTable('*', '/', 128, "KEYS", "VALUES");
        Map<String, Object> kvs = new LinkedHashMap<>();
        kvs.put("k-111", 11111);
        kvs.put("k-22222222222222222222222222222222222", "v-222222222222222222222222222222222222222222222");
        kvs.put("k-3333", "v-3333333333333333333333");
        String content = table.format(kvs);
        logger.info("\n" + content);
    }

    @Test
    public void printTable_1_SetDefaultArgs(){
        DoubleColumnTable table = DoubleColumnTable.newTable("KEYS", "VALUES");
        Map<String, Object> kvs = new LinkedHashMap<>();
        kvs.put("k-111", 11111);
        kvs.put("k-22222222222222222222222222222222222", "v-22222222222222222222222222222222222222222222222222222222222222222222222222222222222222222");
        kvs.put("k-3333", "v-3333333333333333333333");
        String content = table.format(kvs);
        logger.info("\n" + content);
    }
}
