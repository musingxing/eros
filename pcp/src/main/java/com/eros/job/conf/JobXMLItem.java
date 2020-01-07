package com.eros.job.conf;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Used to save job configuration
 *
 * @author Eros
 * @since   2020-01-02 15:58
 */
@XStreamAlias("item")
public class JobXMLItem {

    @XStreamAsAttribute
    private String key;
    @XStreamAsAttribute
    private String value;
    @XStreamAsAttribute
    private String dataType;
    @XStreamAsAttribute
    private String desc;

    public JobXMLItem(String key, String value, String dataType, String desc) {
        this.key = key;
        this.desc = desc;
        this.dataType = dataType;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("<item %s %s %s %s/>",
                key==null?"": "key=\""+key+"\"",
                value==null?"": "value=\""+value+"\"",
                dataType==null?"": "dataType=\""+dataType+"\"",
                desc==null?"": "desc=\""+desc+"\"");
    }
}
