//package com.eros.common.util;
//
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runners.MethodSorters;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.atomic.LongAdder;
//import java.util.concurrent.locks.LockSupport;
//import java.util.logging.Logger;
//
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class LoggerUtilCompareTest {
//
//    private static long MAX_LINE_NUMBER = 100_0000L;
//
//    @Test
//    public void testJavaLog(){
//        testLog(true);
//    }
//
//    @Test
//    public void testLog4j(){
////        testLog(false);
//    }
//
//    private void testLog(boolean isJavaLog){
//        long startTime = System.currentTimeMillis();
//        List<MyThread> threads = new ArrayList<>();
//        threads.add(new MyThread(new TestClass1(isJavaLog)));
//        threads.add(new MyThread(new TestClass1(isJavaLog)));
//        threads.add(new MyThread(new TestClass1(isJavaLog)));
//        threads.add(new MyThread(new TestClass2(isJavaLog)));
//        threads.add(new MyThread(new TestClass2(isJavaLog)));
//        threads.add(new MyThread(new TestClass2(isJavaLog)));
//        threads.add(new MyThread(new TestClass3(isJavaLog)));
//        threads.add(new MyThread(new TestClass3(isJavaLog)));
//        threads.add(new MyThread(new TestClass3(isJavaLog)));
//        threads.add(new MyThread(new TestClass3(isJavaLog)));
//
//        for(Thread t : threads){
//            t.start();
//        }
//
//        boolean stop = false;
//        int sleepCount = 0;
//        START: while(!stop){
//            LockSupport.parkNanos(10*1000*1000L);
//            sleepCount++;
//            if(sleepCount >= 300){
//                System.out.println("Number: " + MyThread.COUNT.longValue() + " time: " + (System.currentTimeMillis()-startTime) + "ms");
//                sleepCount = 0;
//            }
//
//            for(MyThread t : threads){
//                if(!t.stopped)
//                    continue START;
//            }
//            stop = true;
//        }
//        long endTime = System.currentTimeMillis();
//        System.out.println("time: " + (endTime-startTime) + "ms");
//    }
//
//    private static class MyThread extends Thread{
//        private static LongAdder COUNT = new LongAdder();
//        private volatile boolean stopped = false;
//        private final TestClass testClass;
//
//        MyThread(TestClass testClass){
//            this.testClass = testClass;
//        }
//
//        @Override
//        public void run() {
//            while(true){
//                COUNT.increment();
//                testClass.log();
//                if(COUNT.longValue() >= MAX_LINE_NUMBER)
//                    break;
//            }
//            stopped = true;
//        }
//    }
//
//    private interface TestClass{
//        void log();
//    }
//
//    private static class TestClass1 implements TestClass{
//        private static final Logger logger = LoggerUtil.getLogger(TestClass1.class);
//        private static final org.apache.log4j.Logger javaLogger = com.eros.common.util.Log4jUtil.getLogger(TestClass1.class);
//        private static LongAdder COUNT = new LongAdder();
//        private final boolean isJavaLog;
//
//        public TestClass1(boolean isJavaLog) {
//            this.isJavaLog = isJavaLog;
//        }
//
//        public void log(){
//            COUNT.increment();
//            String content = COUNT.longValue() + " This is a test logger..............................";
//            if(isJavaLog)
//                javaLogger.info(content);
//            else
//                logger.info(content);
//        }
//    }
//
//    private static class TestClass2 implements TestClass{
//        private static final Logger logger = LoggerUtil.getLogger(TestClass2.class);
//        private static final java.util.logging.Logger javaLogger = LoggerUtils.getLogger(TestClass2.class);
//        private static LongAdder COUNT = new LongAdder();
//        private final boolean isJavaLog;
//
//        public TestClass2(boolean isJavaLog) {
//            this.isJavaLog = isJavaLog;
//        }
//
//        public void log(){
//            COUNT.increment();
//            String content = COUNT.longValue() + " This is a test logger..............................";
//            if(isJavaLog)
//                javaLogger.info(content);
//            else
//                logger.info(content);
//        }
//    }
//
//    private static class TestClass3 implements TestClass{
//        private static final Logger logger = LoggerUtil.getLogger(TestClass3.class);
//        private static final java.util.logging.Logger javaLogger = LoggerUtils.getLogger(TestClass3.class);
//        private static LongAdder COUNT = new LongAdder();
//        private final boolean isJavaLog;
//
//        public TestClass3(boolean isJavaLog) {
//            this.isJavaLog = isJavaLog;
//        }
//
//        public void log(){
//            COUNT.increment();
//            String content = COUNT.longValue() + " This is a test logger..............................";
//            if(isJavaLog)
//                javaLogger.info(content);
//            else
//                logger.info(content);
//        }
//    }
//}
