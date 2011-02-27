package com.github.dverstap.munin4j.core;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GraphConfig {

    private final String name;
    private final Map<GraphAttributeType, Object> attributeMap;
    private final List<FieldConfig> fieldConfigs; // TODO make this a Map again and check uniqueness on put

    public GraphConfig(String name, Map<GraphAttributeType, Object> attributeMap, List<FieldConfig> fieldConfigs) {
        this.name = name;

        boolean hasBorrowedDataSources = false;
        for (FieldConfig field : fieldConfigs) {
            field.setGraphConfig(this);
            if (field.getBorrowedFieldConfig() != null) {
                hasBorrowedDataSources = true;
            }
        }

        this.fieldConfigs = Collections.unmodifiableList(fieldConfigs);
        if (hasBorrowedDataSources && !attributeMap.containsKey(GraphAttributeType.GRAPH_ORDER)) {
            attributeMap.put(GraphAttributeType.GRAPH_ORDER, buildGraphOrder());
        }
        this.attributeMap = Collections.unmodifiableMap(attributeMap);
    }

    private String buildGraphOrder() {
        StringBuilder result = new StringBuilder();
        for (FieldConfig fieldConfig : fieldConfigs) {
            if (fieldConfig.getBorrowedFieldConfig() == null) {
                result.append(fieldConfig.getName());
            } else {
                result.append(fieldConfig.getName())
                        .append("=")
                        .append(fieldConfig.getBorrowedFieldConfig().getGraphConfig().getName())
                        .append(".")
                        .append(fieldConfig.getBorrowedFieldConfig().getName());
            }
            result.append(" ");
        }
        return result.toString();
    }

    public String getName() {
        return name;
    }

    public void send(LineWriter out) {
        for (Map.Entry<GraphAttributeType, Object> entry : attributeMap.entrySet()) {
            out.writeLine(entry.getKey().getMuninName() + " " + entry.getValue());
        }
        for (FieldConfig fieldConfig : fieldConfigs) {
            fieldConfig.send(out);
        }
        out.writeLine(".");
    }

}
