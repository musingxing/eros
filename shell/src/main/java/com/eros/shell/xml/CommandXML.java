package com.eros.shell.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Used to get command conf
 *
 * @author Eros
 * @since   2020-01-02 15:58
 */
@XStreamAlias("command")
public class CommandXML {

    @XStreamAsAttribute
    private String clazz;
    @XStreamAsAttribute
    private String desc;

    public CommandXML(String clazz, String desc) {
        this.clazz = clazz;
        this.desc = desc;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return String.format("<command %s %s/>",
                clazz==null?"": "clazz=\""+clazz+"\"",
                desc==null?"": "desc=\""+desc+"\"");
    }
}
