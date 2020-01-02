package com.eros.common.util;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoggerUtilTest {

    private static final String LOG_FILE_NAME = "test";
    private static final Logger logger = LoggerUtil.getLogger(LOG_FILE_NAME, LoggerUtilTest.class);

    @Test
    public void printLog_1_Debug(){
        LoggerUtil.resetLogLevel(Level.DEBUG);
        if (logger.isDebugEnabled())
            logger.debug("printing debug log");
        if (logger.isInfoEnabled())
            logger.info("printing info log");
        logger.warn("printing warn log");
    }

    @Test
    public void printLog_2_Info(){
        LoggerUtil.resetLogLevel(Level.INFO);
        if (logger.isDebugEnabled())
            logger.debug("printing debug log");
        if (logger.isInfoEnabled())
            logger.info("printing info log");
        logger.warn("printing warn log");
    }

    @Test
    public void printLog_3_Warn(){
        LoggerUtil.resetLogLevel(Level.WARN);
        if (logger.isDebugEnabled())
            logger.debug("printing debug log");
        if (logger.isInfoEnabled())
            logger.info("printing info log");
        logger.warn("printing warn log");
    }

    @Test
    public void printLog_4_Error(){
        logger.error("printing error log, exception cause:", new Exception("test"));
    }
}
