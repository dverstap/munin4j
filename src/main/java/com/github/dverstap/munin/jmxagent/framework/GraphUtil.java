package com.github.dverstap.munin.jmxagent.framework;

import java.util.LinkedHashMap;
import java.util.Map;

public class GraphUtil {

    public static String buildName(String str) {
        str = str.toLowerCase();
        StringBuilder result = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (Character.isJavaIdentifierPart(ch)) {
                result.append(ch);
            } else {
                result.append("_");
            }
        }
        return result.toString();
    }

    public static Map<FieldConfig, Object> build(FieldConfig key1, Object value1) {
        Map<FieldConfig, Object> result = new LinkedHashMap<FieldConfig, Object>();
        result.put(key1, value1);
        return result;
    }

    public static Map<FieldConfig, Object> build(FieldConfig key1, Object value1,
                                                 FieldConfig key2, Object value2) {
        Map<FieldConfig, Object> result = new LinkedHashMap<FieldConfig, Object>();
        result.put(key1, value1);
        result.put(key2, value2);
        return result;
    }

    public static Map<FieldConfig, Object> build(FieldConfig key1, Object value1,
                                                 FieldConfig key2, Object value2,
                                                 FieldConfig key3, Object value3) {
        Map<FieldConfig, Object> result = new LinkedHashMap<FieldConfig, Object>();
        result.put(key1, value1);
        result.put(key2, value2);
        result.put(key3, value3);
        return result;
    }

    public static Map<FieldConfig, Object> build(FieldConfig key1, Object value1,
                                                 FieldConfig key2, Object value2,
                                                 FieldConfig key3, Object value3,
                                                 FieldConfig key4, Object value4) {
        Map<FieldConfig, Object> result = new LinkedHashMap<FieldConfig, Object>();
        result.put(key1, value1);
        result.put(key2, value2);
        result.put(key3, value3);
        result.put(key4, value4);
        return result;
    }

    public static Map<FieldConfig, Object> build(FieldConfig key1, Object value1,
                                                 FieldConfig key2, Object value2,
                                                 FieldConfig key3, Object value3,
                                                 FieldConfig key4, Object value4,
                                                 FieldConfig key5, Object value5) {
        Map<FieldConfig, Object> result = new LinkedHashMap<FieldConfig, Object>();
        result.put(key1, value1);
        result.put(key2, value2);
        result.put(key3, value3);
        result.put(key4, value4);
        result.put(key5, value5);
        return result;
    }
}
