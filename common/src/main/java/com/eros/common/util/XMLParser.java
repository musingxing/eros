package com.eros.common.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;

import java.lang.annotation.Annotation;

/**
 * Parser to process .xml file
 *
 * @author Eros
 * @since 2020-01-02 15:58
 */
public class XMLParser {
    /**
     * XML to JvaBean
     *
     * @param xml   xml char seq
     * @param clazz class
     */
    public static Object parseObjectFromXML(String xml, Class clazz) {
        XStream xStream = new XStream();
        XStream.setupDefaultSecurity(xStream);
        xStream.allowTypes(new Class[]{clazz});
        xStream.autodetectAnnotations(true);
        xStream.processAnnotations(clazz);
        return xStream.fromXML(xml);
    }

    /**
     * JavaBean to xml
     *
     * @param obj java object
     * @return String xml
     */
    public static String toXml(Object obj) {
        XStream xstream = new XStream(new DomDriver("utf-8", new XmlFriendlyReplacer("-_", "_")));
        xstream.autodetectAnnotations(true);
        xstream.processAnnotations(obj.getClass());
        return xstream.toXML(obj);
    }

    /*
     * XML to JvaBean
     *
     * @param xmlStr    xml string
     * @param cls       xml and its class
     *
     * <T> T
     * @return T instance
     */
    public static <T> T parseXML(String xmlStr, Class<T> cls) {
        XStream xstream = new XStream(new DomDriver("utf-8", new XmlFriendlyReplacer("-_", "_")));
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypes(new Class[]{cls});
        xstream.autodetectAnnotations(true);
        xstream.processAnnotations(cls);
        T obj = (T) xstream.fromXML(xmlStr);
        return obj;
    }
}