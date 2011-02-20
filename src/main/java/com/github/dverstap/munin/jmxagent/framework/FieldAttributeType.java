package com.github.dverstap.munin.jmxagent.framework;

// http://munin-monitoring.org/wiki/protocol-config
public enum FieldAttributeType {

    LABEL("label"),
    TYPE("type"),
    INFO("info"),
    CDEF("cdef"),
    DRAW("draw"),
    MIN("min"),
    MAX("max");

    private final String muninName;

    FieldAttributeType(String muninName) {
        this.muninName = muninName;
    }

    public String getMuninName() {
        return muninName;
    }
}
