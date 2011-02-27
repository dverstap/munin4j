package com.github.dverstap.munin4j.core;

import java.util.Collections;
import java.util.Map;

// TODO rename to DataSourceConfig
public class FieldConfig {

    private final String name;
    private final FieldConfig borrowedFieldConfig;
    private final Map<FieldAttributeType, Object> attributeMap;
    private GraphConfig graphConfig;

    public FieldConfig(String name, FieldConfig borrowedFieldConfig, Map<FieldAttributeType, Object> attributeMap) {
        this.name = name;
        this.borrowedFieldConfig = borrowedFieldConfig;
        this.attributeMap = Collections.unmodifiableMap(attributeMap);
    }

    public String getName() {
        return name;
    }

    public FieldConfig getBorrowedFieldConfig() {
        return borrowedFieldConfig;
    }

    public void send(LineWriter out) {
        for (Map.Entry<FieldAttributeType, Object> entry : attributeMap.entrySet()) {
            out.writeLine(name + "." + entry.getKey().getMuninName() + " " + entry.getValue());
        }
    }

    public GraphConfig getGraphConfig() {
        return graphConfig;
    }

    public Map<FieldAttributeType, Object> getAttributeMap() {
        return attributeMap;
    }

    void setGraphConfig(GraphConfig graphConfig) {
        if (this.graphConfig != null) {
            throw new IllegalStateException("graphConfig cannot be modified once it is set.");
        }
        this.graphConfig = graphConfig;
    }
}
