package com.eros.common.util;


import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.LockSupport;
import java.util.logging.*;

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
    private static final ThreadLocal<SimpleDateFormat> DATE_FORMATTER_ = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        }
    };
    /** *******************************  Log Properties Def *********************** */
    private static final char SEQ = ' ';
    private static final String LOG_HOME = System.getProperty("com.eros.home");
    private static final String LOG_FILE_SUFFIX = ".log";

    private static final Map<Class<?>, Logger> LOGGERS = new ConcurrentHashMap<Class<?>, Logger>();
    private static final Set<Thread> THREADS = Collections.newSetFromMap(new ConcurrentHashMap<Thread, Boolean>(10));
    private static volatile Level LOG_LEVEL = Level.INFO;
    private static volatile boolean LOG_LEVEL_RESET_OPEN = false;
    private static volatile Formatter LOG_RECORD_FORMAT = new Formatter() {
        @Override
        public String format(LogRecord record) {
            String date = DATE_FORMATTER_.get().format(record.getMillis());
            String threadName = "[" + Thread.currentThread().getName() + "]";
            String level = record.getLevel().getName();
            String clazz = record.getSourceClassName() + "." + record.getSourceMethodName() + "()";
            String content = "-" + record.getMessage();
            String log = date + SEQ + threadName + SEQ + level + SEQ + clazz + SEQ + content;
            String throwable = "";
            if (record.getThrown() != null) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                pw.println();
                pw.print("cause: ");
                record.getThrown().printStackTrace(pw);
                pw.close();
                throwable = sw.toString();
            }
            return throwable.isEmpty() ? (log+"\n") : (log + throwable);
        }
    };

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
            logger = Logger.getLogger(clazz.getName());
            logger.setLevel(LOG_LEVEL);
            logger.setUseParentHandlers(false);

            // handler
            FileHandler fileHandler = new FileHandler(logFile);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setEncoding("utf-8");
            fileHandler.setFormatter(LOG_RECORD_FORMAT);
            logger.addHandler(fileHandler);

            // console
            if(openConsole){
                ConsoleHandler consoleHandler = new ConsoleHandler();
                consoleHandler.setLevel(Level.ALL);
                consoleHandler.setEncoding("utf-8");
                consoleHandler.setFormatter(LOG_RECORD_FORMAT);
                logger.addHandler(consoleHandler);
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
    public static Logger getTestLogger(Class<?> clazz){
        return getLogger("test", clazz, true, true);
    }

    /**
     * Getter Logger in default model
     *
     * @param clazz      Class
     * @return           Logger
     */
    public static Logger getConsoleLogger(Class<?> clazz){
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

    public static void main(String[] args) {
        Logger logger = LoggerUtil.getConsoleLogger(Object.class);
        logger.info("This is a test log");
        logger.info("This is a test log");
        logger.log(Level.INFO, "This is a exception log", new RuntimeException("fail ..."));//A bug for jdk
        LogRecord record = new LogRecord(Level.SEVERE,  "This is a exception log");
        record.setThrown(new RuntimeException("fail ..."));
        logger.log(record);
    }
}