package com.github.dverstap.munin.jmxagent;

import java.util.EnumMap;
import java.util.Map;

public class GraphBuilder {

    private final Map<GraphAttributeType, Object> attributes = new EnumMap<GraphAttributeType, Object>(GraphAttributeType.class);

    public GraphBuilder title(String title) {
        attributes.put(GraphAttributeType.GRAPH_TITLE, title);
        return this;
    }

    public GraphBuilder vLabel(String vLabel) {
        attributes.put(GraphAttributeType.GRAPH_VLABEL, vLabel);
        return this;
    }

    public String build() {
        StringBuilder b = new StringBuilder();
        for (Map.Entry<GraphAttributeType, Object> entry : attributes.entrySet()) {
            b.append(entry.getKey().getMuninName())
            .append(" ")
            .append(entry.getValue())
            .append("\n");
        }
        b.append(".\n");
        return b.toString();
    }

}
