package com.github.dverstap.munin.jmxagent;

import java.util.LinkedHashMap;
import java.util.Map;

public class GraphConfigBuilder {

    private final String name;
    private final Map<GraphAttributeType, Object> attributeMap = new LinkedHashMap<GraphAttributeType, java.lang.Object>();
    private final Map<String, FieldConfig> fieldConfigMap = new LinkedHashMap<String, FieldConfig>();

    public GraphConfigBuilder(String name) {
        this.name = name;
    }

    public GraphConfigBuilder title(String title) {
        attributeMap.put(GraphAttributeType.GRAPH_TITLE, title);
        return this;
    }

    public GraphConfigBuilder vLabel(String vLabel) {
        attributeMap.put(GraphAttributeType.GRAPH_VLABEL, vLabel);
        return this;
    }

    public GraphConfig build() {
        return new GraphConfig(name, attributeMap, fieldConfigMap);
    }

    public GraphConfigBuilder fields(FieldConfig... fields) {
        for (FieldConfig field : fields) {
            fieldConfigMap.put(field.getName(), field);
        }
        return this;
    }
}
