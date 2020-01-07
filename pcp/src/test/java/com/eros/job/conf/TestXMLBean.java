package com.eros.job.conf;

import com.eros.common.util.LoggerUtil;
import com.eros.common.util.XMLParser;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestXMLBean {

    private static final Logger logger = LoggerUtil.getLogger("test", TestXMLBean.class);

    @Test
    public void test_1_JobXMLItem(){
        JobXMLItem item = new JobXMLItem("com.eros.job.name", "job name", "string","eros");
        String content = XMLParser.toXml(item);
        logger.info("JobXMLItem to XML: \n" + content);

        Object result = XMLParser.parseObjectFromXML(content, JobXMLItem.class);
        logger.info("XML to JobXMLItem: \n" + result);

        Assert.assertEquals(content, result.toString());
    }

    @Test
    public void test_2_JobXMLItems(){
        List<JobXMLItem> items = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            JobXMLItem item = new JobXMLItem("com.eros.job.name"+i, "job name"+i, "string","eros"+i);
            items.add(item);
        }
        JobXMLItems jobXMLItems = new JobXMLItems(items, "job1111");
        String content = XMLParser.toXml(jobXMLItems);
        logger.info("JobXMLItem to XML: \n" + content);

        Object result = XMLParser.parseObjectFromXML(content, JobXMLItems.class);
        logger.info("XML to JobXMLItem: \n" + result);

        Assert.assertEquals(content, result.toString());
    }
}
