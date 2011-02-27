package com.github.dverstap.munin4j.jdk;

public enum MemoryUsageField {

    INIT("Init"),
    USED("Used"),
    COMMITTED("Committed"),
    MAX("Max");

    private final String label;

    MemoryUsageField(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
