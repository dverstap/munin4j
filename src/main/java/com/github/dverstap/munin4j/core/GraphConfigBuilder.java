package com.github.dverstap.munin4j.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GraphConfigBuilder {

    private final String name;
    private final Map<GraphAttributeType, Object> attributeMap = new LinkedHashMap<GraphAttributeType, Object>();
    private final List<FieldConfig> fieldConfigs = new ArrayList<FieldConfig>();

    public GraphConfigBuilder(String name) {
        this.name = name;
    }

    public GraphConfig build() {
        return new GraphConfig(name, attributeMap, fieldConfigs);
    }

    private GraphConfigBuilder put(GraphAttributeType key, Object value) {
        attributeMap.put(key, value);
        return this;
    }

    public GraphConfigBuilder fields(FieldConfig... fields) {
        fieldConfigs.addAll(Arrays.asList(fields));
        return this;
    }

    public GraphConfigBuilder fields(Collection<FieldConfig> fields) {
        fieldConfigs.addAll(fields);
        return this;
    }

    public GraphConfigBuilder title(String title) {
        return put(GraphAttributeType.GRAPH_TITLE, title);
    }

    public GraphConfigBuilder vLabel(String vLabel) {
        return put(GraphAttributeType.GRAPH_VLABEL, vLabel);
    }
    public GraphConfigBuilder category(String category) {
        return put(GraphAttributeType.GRAPH_CATEGORY, category);
    }

    public GraphConfigBuilder info(String info) {
        return put(GraphAttributeType.GRAPH_INFO, info);
    }

    public GraphConfigBuilder args(String args) {
        return put(GraphAttributeType.GRAPH_ARGS, args);
    }

}
