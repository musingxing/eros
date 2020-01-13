package com.eros.common.util;


import org.apache.log4j.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.LockSupport;

/**
 * Utility class to manage log printer
 *
 * @author Eros
 * @since   2020-01-02 15:58
 */
public class LoggerUtil {

    private static final ThreadLocal<SimpleDateFormat> DATE_FORMATTER = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };
    /** *******************************  Log Properties Def *********************** */
    private static final String LOG_HOME = System.getProperty("com.eros.home");
    private static final String LOG_FILE_SUFFIX = ".log";
    private static final String LAY_OUT_PATTERN = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %-5p %c.%M():%L -%m%n";
    private static final long DEFAULT_MAX_FILE_SIZE = 50L *1024L *1024L;

    private static final Map<Class<?>, Logger> LOGGERS = new ConcurrentHashMap<Class<?>, Logger>();
    private static final Set<Thread> THREADS = Collections.newSetFromMap(new ConcurrentHashMap<Thread, Boolean>(10));
    private static volatile Level LOG_LEVEL = Level.INFO;
    private static volatile boolean LOG_LEVEL_RESET_OPEN = false;

    /**
     * Getter Logger
     *
     * @param fileName   Log File
     * @param clazz      Class
     * @return           Logger
     */
    public static Logger getLogger(String fileName, Class<?> clazz, boolean hasDate, boolean openConsole){

        // waiting for LOG-LEVEL to be reset successfully
        while(LOG_LEVEL_RESET_OPEN){
            LockSupport.parkNanos(10*1000*1000L);
        }

        // Logger corresponding to Class
        Logger logger = LOGGERS.get(clazz);
        if(logger != null)
            return logger;

        String logFile = genFilePath(fileName, hasDate);
        boolean ifAddThread = false;
        try{
            // record who is doing this
            if(!THREADS.contains(Thread.currentThread())){
                ifAddThread = THREADS.add(Thread.currentThread());
            }

            // logger
            logger = LogManager.getLogger(clazz);
            logger.setLevel(LOG_LEVEL);

            // appender
            RollingFileAppender appender = new RollingFileAppender();
            appender.setFile(logFile);
            appender.setEncoding("utf-8");
            appender.setAppend(true);
            appender.setMaximumFileSize(DEFAULT_MAX_FILE_SIZE);

            PatternLayout patternLayout = new PatternLayout();
            patternLayout.setConversionPattern(LAY_OUT_PATTERN);
            appender.setLayout(patternLayout);
            appender.activateOptions();
            logger.addAppender(appender);

            if(openConsole){
                // console appender
                ConsoleAppender consoleAppender = new ConsoleAppender();
                consoleAppender.setEncoding("utf-8");

                PatternLayout consolePatternLayout = new PatternLayout();
                consolePatternLayout.setConversionPattern(LAY_OUT_PATTERN);
                consoleAppender.setLayout(patternLayout);
                consoleAppender.activateOptions();
                logger.addAppender(consoleAppender);
            }

            LOGGERS.put(clazz, logger);
            return logger;
        }
        catch (Throwable e){
            throw new RuntimeException(String.format("Configure log:%s failure, Class:%s", fileName, clazz), e);
        }
        finally {
            // completed and remove who do this
            if(ifAddThread){
                THREADS.remove(Thread.currentThread());
            }
        }
    }

    /**
     * Generate the path of log file </br>
     * path [$HOME/logs/$FILE_NAME.$FILE_TYPE.$Date] </br>
     * e.g. [/home/logs/file.csv.2020-01-02]
     *
     * @param fileName  file name
     * @param hasDate   if has date as suffix
     * @return file path
     */
    private static String genFilePath(String fileName, boolean hasDate){
        String logHome = (LOG_HOME != null ? LOG_HOME+ File.separator+"logs"+File.separator : "");
        String fileNameStr = fileName + LOG_FILE_SUFFIX;
        String dateStr = (hasDate ? "."+DATE_FORMATTER.get().format(System.currentTimeMillis()) : "");
        return logHome + fileNameStr + dateStr;
    }

    /**
     * Getter Logger
     *
     * @param fileName   Log File
     * @param clazz      Class
     * @return           Logger
     */
    public static Logger getLogger(String fileName, Class<?> clazz){
        return getLogger(fileName, clazz, true, false);
    }

    /**
     * Getter Logger in default model
     *
     * @param clazz      Class
     * @return           Logger
     */
    public static Logger getLogger(Class<?> clazz){
        return getLogger("eros", clazz);
    }

    /**
     * Getter Logger in default model
     *
     * @param clazz      Class
     * @return           Logger
     */
    public static Logger getLoggerOnConsole(Class<?> clazz){
        return getLogger("eros", clazz, true, true);
    }

    /**
     * Reset log level
     *
     * @param level   Log Level e.g. 'DEBUG', 'INFO', 'WARN'.
     */
    public static void resetLogLevel(Level level){

        // Set {@code LOG_LEVEL_RESET_OPEN} true
        LOG_LEVEL_RESET_OPEN = true;
        // waiting for completed who do this
        while(!THREADS.isEmpty()){
            LockSupport.parkNanos(10*1000*1000L);
        }
        for( Logger logger : LOGGERS.values()){
            logger.setLevel(level);
        }
        LOG_LEVEL_RESET_OPEN = false;
    }
}
