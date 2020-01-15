package com.eros.shell.test;

import com.eros.common.util.LoggerUtil;
import com.eros.shell.main.ErosMain;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.logging.Level;
import java.util.logging.Logger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ErosMainTest {

    private static final Logger logger = LoggerUtil.getTestLogger(ErosMainTest.class);
    private ErosMain main = new ErosMain(null);

    private void parseAndExec(String commandStr){
        try{
            logger.info("Start exec command(step 1-parse, step 2-exec): '" + commandStr + "'");
            main.executeLine(commandStr);
            logger.info("Success to exec command: " + commandStr);
        }catch (Throwable e){
            logger.log(Level.SEVERE, "Fail to exec command: " + commandStr, e);
        }
    }

    @Test
    public void helpCommandTest(){
        String commandStr = "help";
        parseAndExec(commandStr);
    }


    @Test
    public void test_dmaker_stop(){
        String commandStr = "dmaker stop";
        parseAndExec(commandStr);
    }

    @Test
    public void test_history(){
        String commandStr = "history -n 10";
        parseAndExec(commandStr);
    }
}
