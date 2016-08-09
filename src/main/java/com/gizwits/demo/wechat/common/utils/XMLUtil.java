package com.gizwits.demo.wechat.common.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.io.xml.XppDriver;

import java.io.InputStream;
import java.io.Writer;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by matt on 16-5-23.
 */
public class XMLUtil {

    public static Object getWxMsg(Class clazz, String xml) {
        XStream xstream = new XStream(new StaxDriver());
        xstream.alias("xml", clazz);
        return xstream.fromXML(xml);
    }

    public static Object getWxMsg(Class clazz, InputStream xml) {
        XStream xstream = new XStream(new StaxDriver());
        xstream.alias("xml", clazz);
        return xstream.fromXML(xml);
    }

    public static String toXML(Object object) {
        XStream xstream = new XStream(
                new XppDriver() {
                    public HierarchicalStreamWriter createWriter(Writer out) {
                        return new PrettyPrintWriter(out) {
                            boolean cdata = false;
                            public void startNode(String name, Class clazz){
                                super.startNode(name, clazz);
                                cdata = clazz.toString().equals(String.class.toString());
                            }
                            protected void writeText(QuickWriter writer, String text) {
                                if(cdata) {
                                    writer.write("<![CDATA[");
                                    writer.write(text);
                                    writer.write("]]>");
                                } else {
                                    writer.write(text);
                                }
                            }
                        };
                    }
                }
        );
        xstream.alias("xml", object.getClass());
        return xstream.toXML(object);
    }

    /**
     * 将一个数据流，转化为XML字符串
     * @param inputStream
     * @return
     */
    public static String InputStream2String(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

    /**
     * 将一个xml转化成Map
     * @param xml
     * @return
     */
    public static Map XML2Map(String xml) {
        XStream xstream = new XStream();
        xstream.registerConverter(new MapEntryConverter());
        xstream.alias("xml", Map.class);
        return (Map) xstream.fromXML(xml);
    }

    public static Map XML2Map(InputStream xml) {
        XStream xstream = new XStream();
        xstream.registerConverter(new MapEntryConverter());
        xstream.alias("xml", Map.class);
        return (Map) xstream.fromXML(xml);
    }


    static class MapEntryConverter implements Converter {

        public boolean canConvert(Class clazz) {
            return AbstractMap.class.isAssignableFrom(clazz);
        }

        public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {

            AbstractMap map = (AbstractMap) value;
            for (Object obj : map.entrySet()) {
                Map.Entry entry = (Map.Entry) obj;
                writer.startNode(entry.getKey().toString());
                Object val = entry.getValue();
                if ( null != val ) {
                    writer.setValue(val.toString());
                }
                writer.endNode();
            }

        }

        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {

            Map<String, String> map = new HashMap<String, String>();

            while(reader.hasMoreChildren()) {
                reader.moveDown();

                String key = reader.getNodeName(); // nodeName aka element's name
                String value = reader.getValue();
                map.put(key, value);

                reader.moveUp();
            }

            return map;
        }

    }

}
