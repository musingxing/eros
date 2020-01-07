package com.eros.job.conf;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import javax.xml.bind.annotation.XmlList;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to save job configuration
 *
 * @author Eros
 * @since   2020-01-02 15:58
 */
@XStreamAlias("job")
public class JobXMLItems {

    @XStreamAsAttribute
    private String name;

    @XStreamImplicit(itemFieldName = "item")
    @XmlList
    private List<JobXMLItem> items = new ArrayList<>();

    public JobXMLItems(List<JobXMLItem> items, String name) {
        this.items = items;
        this.name = name;
    }

    public List<JobXMLItem> getItems() {
        return items;
    }

    public void setItems(List<JobXMLItem> items) {
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("<job");
        if(name != null)
            stringBuilder.append(" name=\"").append(name).append("\"");
        stringBuilder.append(">").append("\n");
        if(items != null && !items.isEmpty()){
            for(JobXMLItem item : items){
                stringBuilder.append("  ").append(item).append("\n");
            }
        }
        stringBuilder.append("</job>");
        return stringBuilder.toString();
    }
}
