package com.eros.common.util;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.logging.Level;
import java.util.logging.Logger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoggerUtilTest {

    private static final Logger logger = LoggerUtil.getTestLogger(LoggerUtilTest.class);

    @Test
    public void printLog_2_Info() {
        LoggerUtil.resetLogLevel(Level.INFO);
        if (logger.isLoggable(Level.INFO))
            logger.info("printing debug log");
        if (logger.isLoggable(Level.WARNING))
            logger.warning("printing warn log");
    }

    @Test
    public void printLog_3_Warn() {
        LoggerUtil.resetLogLevel(Level.WARNING);
        if (logger.isLoggable(Level.INFO))
            logger.info("printing info log");
        if (logger.isLoggable(Level.INFO))
            logger.warning("printing warn log");
    }

    @Test
    public void printLog_4_Error() {
        logger.log(Level.SEVERE, "printing error log, exception cause:", new Exception("test"));
    }
}
