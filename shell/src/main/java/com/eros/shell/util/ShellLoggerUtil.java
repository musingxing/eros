package com.eros.shell.util;

import com.eros.common.util.LoggerUtil;
import org.apache.log4j.Logger;

/**
 * Logger for shell system.
 *
 * @author Eros
 * @since   2020-01-03 10:58
 */
public class ShellLoggerUtil {

    private static final String LOG_FILE = "shell";

    /**
     * Getter Logger in default model
     *
     * @param clazz      Class
     * @return           Logger
     */
    public static Logger getLogger(Class<?> clazz){
        return LoggerUtil.getLogger(LOG_FILE, clazz, true, false);
    }
}
