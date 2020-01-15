package com.eros.local;

import com.eros.common.util.LoggerUtil;
import com.eros.datagen.local.LocalFileManager;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocalFileManagerTest {

    private static Logger logger = LoggerUtil.getTestLogger( LocalFileManagerTest.class);
    @Test
    public void test_1_GenerateFileName(){
        List<String> dirs = new ArrayList<>();
        dirs.add("/data1/dir/files");
        dirs.add("/data2/dir/files");
        dirs.add("/data3/dir/files");

        String filePrefix = "my_file";
        String fileType = ".txt";
        LocalFileManager fileManager = LocalFileManager.newFileManager(dirs, filePrefix, fileType);

        logger.info(String.format("\nDirs:%s\nFile Prefix:%s\nFile Type:%s\n", dirs, filePrefix, fileType));
        int fileNum = 100;
        for(int i = 0; i < 100; i++){
            String fileName = fileManager.nextFileName();
            logger.info("File: " + fileName);
        }
    }

    @Test
    public void test_2_GenerateFileName_Parallel(){
        List<String> dirs = new ArrayList<>();
        dirs.add("/data1/dir/files");
        dirs.add("/data2/dir/files");
        dirs.add("/data3/dir/files");

        String filePrefix = "my_file";
        String fileType = ".txt";
        LocalFileManager fileManager = LocalFileManager.newFileManager(dirs, filePrefix, fileType);

        List<Runnable> tasks = new ArrayList<>();
        int parallel = 100;
        for(int i = 0; i < parallel; i++){
            int order = i;
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    Thread.currentThread().setName(("T" + order));
                    for(int j = 0; j < parallel; j++){
                        String fileName = fileManager.nextFileName();
                        logger.info(Thread.currentThread().getName() + "-File: " + fileName);
                    }
                }
            };
            tasks.add(task);
        }

        for(Runnable task : tasks){
            new Thread(task).start();
        }

        try{
            Thread.sleep(10*1000L);
        }catch (Throwable e){
        }
    }
}
