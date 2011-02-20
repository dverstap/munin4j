package com.github.dverstap.munin.jmxagent.framework;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GraphConfig {

    private final String name;
    private final Map<GraphAttributeType, Object> attributeMap;
    private final List<FieldConfig> fieldConfigs;

    public GraphConfig(String name, Map<GraphAttributeType, Object> attributeMap, List<FieldConfig> fieldConfigs) {
        this.name = name;

        boolean hasBorrowedDataSources = false;
        for (FieldConfig field : fieldConfigs) {
            if (field.getOriginalGraphConfig() == null) {
                field.setOriginalGraphConfig(this);
            } else {
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
            if (fieldConfig.getOriginalGraphConfig() == this) {
                result.append(fieldConfig.getName());
            } else {
                result.append(fieldConfig.getName())
                        .append("=")
                        .append(fieldConfig.getOriginalGraphConfig().getName())
                        .append(".")
                        .append(fieldConfig.getName());
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
            if (fieldConfig.getOriginalGraphConfig() == this) {
                fieldConfig.send(out);
            }
        }
        out.writeLine(".");
    }

}
