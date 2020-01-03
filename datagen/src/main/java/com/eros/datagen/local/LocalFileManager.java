package com.eros.datagen.local;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * File manager to generate next file name
 *
 * @author Eros
 * @since   2020-01-03 17:10
 */
public class LocalFileManager {

    /**
     * File directories
     */
    private final String[] dirs;
    /**
     * File prefix
     */
    private final String filePrefix;
    /**
     * File type
     */
    private final String fileType;
    /**
     * File order
     */
    private final AtomicInteger fileOrder = new AtomicInteger(0);
    private final ThreadLocalRandom random = ThreadLocalRandom.current();
    private final List<String> usedFileNames = Collections.synchronizedList(new ArrayList<>());

    /**
     * Constructor
     *
     * @param dirs             File directories
     * @param filePrefix      File prefix
     * @param fileType        File type
     */
    private LocalFileManager(List<String> dirs, String filePrefix, String fileType){
        this.dirs = new String[dirs.size()];
        for(int i = 0, size = dirs.size(); i < size; i++){
            this.dirs[i] = dirs.get(i);
        }
        this.filePrefix = filePrefix;
        this.fileType = fileType;
    }

    public List<String> getUsedFileNames() {
        return usedFileNames;
    }

    @Override
    public String toString() {
        return "LocalFileManager{" +
                "dirs=" + Arrays.toString(dirs) +
                ", filePrefix='" + filePrefix + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileOrder=" + fileOrder +
                ", random=" + random +
                '}';
    }

    public String nextFileName(){
        int r = random.nextInt(0, dirs.length);
        String dir = dirs[r%dirs.length];
        String fileName = dir + File.separator + filePrefix + "_" + fileOrder.getAndIncrement() + fileType;
        usedFileNames.add(fileName);
        return fileName;
    }

    public static LocalFileManager newFileManager(List<String> dirs, String filePrefix, String fileType){
        // 做校驗，這裏暫時省略
        return new LocalFileManager(dirs, filePrefix, fileType);
    }
}
