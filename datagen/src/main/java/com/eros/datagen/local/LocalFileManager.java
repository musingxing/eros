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
        // check dirs
        for(String dir : dirs){
            File dataDir = new File(dir);
            if(!dataDir.exists())
                throw new RuntimeException("Data directory:" + dataDir.getAbsolutePath() + " not exists");
            if(!dataDir.isDirectory())
                throw new RuntimeException("Data directory:" + dataDir.getAbsolutePath() + " is not directory");
        }
        // check filePrefix
        // check file type
        return new LocalFileManager(dirs, filePrefix, fileType);
    }

    enum FileType{
        CSV(".csv"),
        TXT(".txt");

        private final String fileType;

        FileType(String fileType) {
            this.fileType = fileType;
        }

        public String getFileType() {
            return fileType;
        }

        public static FileType parse(String type){
            if(type == null || type.isEmpty())
                throw new IllegalArgumentException("File type is empty");
            switch (type.toUpperCase()){
                case "CSV":
                    return CSV;
                case "TXT":
                    return TXT;
                default:
                    throw new IllegalArgumentException("Unknown type: " + type);
            }

        }
    }
}
