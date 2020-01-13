package com.eros.shell.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import javax.xml.bind.annotation.XmlList;
import java.util.List;

/**
 * Used to get commands conf
 *
 * @author Eros
 * @since 2020-01-02 15:58
 */
@XStreamAlias("commands")
public class CommandsXML {

    @XStreamAsAttribute
    private String name;

    @XStreamImplicit(itemFieldName = "command")
    @XmlList
    private List<CommandXML> commands;

    public CommandsXML(List<CommandXML> commands, String name) {
        this.commands = commands;
        this.name = name;
    }

    public List<CommandXML> getCommands() {
        return commands;
    }

    public void setCommands(List<CommandXML> commands) {
        this.commands = commands;
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
        if (name != null)
            stringBuilder.append(" name=\"").append(name).append("\"");
        stringBuilder.append(">").append("\n");
        if (commands != null && !commands.isEmpty()) {
            for (CommandXML item : commands) {
                stringBuilder.append("  ").append(item).append("\n");
            }
        }
        stringBuilder.append("</job>");
        return stringBuilder.toString();
    }
}
