package com.github.dverstap.munin.jmxagent.framework;

import java.util.Map;

public class FieldConfig {

    private final String name;
    private final Map<FieldAttributeType, Object> attributeMap;
    private GraphConfig originalGraphConfig;

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

    public GraphConfig getOriginalGraphConfig() {
        return originalGraphConfig;
    }

    void setOriginalGraphConfig(GraphConfig originalGraphConfig) {
        if (this.originalGraphConfig != null) {
            throw new IllegalStateException("originalGraphConfig cannot be modified once it is set.");
        }
        this.originalGraphConfig = originalGraphConfig;
    }
}
