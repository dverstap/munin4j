package com.github.dverstap.munin.jmxagent;

import java.util.Map;

public class FieldConfig {

    private final String name;
    private final Map<FieldAttributeType, Object> attributeMap;

    public FieldConfig(String name, Map<FieldAttributeType, Object> attributeMap) {
        this.name = name;
        this.attributeMap = attributeMap;
    }

    public String getName() {
        return name;
    }

    public void send(LineWriter out) {
        for (Map.Entry<FieldAttributeType, Object> entry : attributeMap.entrySet()) {
            out.writeLine(name + "." + entry.getKey().getMuninName() + " " + entry.getValue());
        }
    }
}
