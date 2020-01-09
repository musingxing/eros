package com.eros.shell.config;

/**
 * Eros Shell Config Items.
 *
 * @author Eros
 * @since   2020-01-03 10:58
 */
public class ErosShellConfig {

    private static volatile boolean ENABLE_TEST_LOG = false;

    public static boolean isEnableTestLog() {
        return ENABLE_TEST_LOG;
    }

    public static void setEnableTestLog(boolean enable){
        ENABLE_TEST_LOG = enable;
    }
}
