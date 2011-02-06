package com.github.dverstap.munin.jmxagent;

import java.util.Collections;
import java.util.Map;

public class GraphConfig {

    private final String name;
    private final Map<GraphAttributeType, Object> attributeMap;
    private final Map<String, FieldConfig> fieldConfigMap;

    public GraphConfig(String name, Map<GraphAttributeType, Object> attributeMap, Map<String, FieldConfig> fieldConfigMap) {
        this.name = name;
        this.attributeMap = Collections.unmodifiableMap(attributeMap);
        this.fieldConfigMap = Collections.unmodifiableMap(fieldConfigMap);
    }

    public String getName() {
        return name;
    }

    public void send(LineWriter out) {
        for (Map.Entry<GraphAttributeType, Object> entry : attributeMap.entrySet()) {
            out.writeLine(entry.getKey().getMuninName() + " " + entry.getValue());
        }
        for (FieldConfig fieldConfig : fieldConfigMap.values()) {
            fieldConfig.send(out);
        }
        out.writeLine(".");
    }

}
