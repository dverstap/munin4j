package com.github.dverstap.munin.jmxagent;

public enum FieldAttributeType {

    NAME("name"),
    LABEL("label"),
    INFO("info"),
    TYPE("type"),
    CDEF("cdef");

    private final String muninName;

    FieldAttributeType(String muninName) {
        this.muninName = muninName;
    }

    public String getMuninName() {
        return muninName;
    }
}
