package com.github.dverstap.munin4j.jdk;

public enum MemoryPoolField {

    USAGE("Usage", "getUsage"),
    PEAK_USAGE("Peak Usage", "getPeakUsage"),
    COLLECTION_USAGE("Collection Usage", "getCollectionUsage");

    private final String label;
    private final String methodName;

    MemoryPoolField(String label, String methodName) {
        this.label = label;
        this.methodName = methodName;
    }

    public String getLabel() {
        return label;
    }

    public String getMethodName() {
        return methodName;
    }
}
