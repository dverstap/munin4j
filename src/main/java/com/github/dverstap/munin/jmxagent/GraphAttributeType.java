package com.github.dverstap.munin.jmxagent;

// http://munin-monitoring.org/wiki/protocol-config
public enum GraphAttributeType {

    GRAPH_TITLE("graph_title"),
    GRAPH_VLABEL("graph_vlabel"),
    GRAPH_SCALE("graph_scale"),
    GRAPH_CATEGORY("graph_category"),
    GRAPH_ARGS("graph_info"),
    GRAPH_INFO("graph_args"),
    GRAPH_PERIOD("graph_period"),
    GRAPH_ORDER("graph_order"),
    GRAPH_PRINTF("graph_printf");

    private final String muninName;

    GraphAttributeType(String name) {
        muninName = name;
    }

    public String getMuninName() {
        return muninName;
    }


}
