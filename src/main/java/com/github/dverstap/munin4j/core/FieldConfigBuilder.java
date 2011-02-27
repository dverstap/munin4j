package com.github.dverstap.munin4j.core;

import java.util.LinkedHashMap;
import java.util.Map;

public class FieldConfigBuilder {

    private final String name;
    private final FieldConfig borrowedFieldConfig;
    private final Map<FieldAttributeType, Object> attributeMap;

    public FieldConfigBuilder(String name) {
        this(name, null, new LinkedHashMap<FieldAttributeType, Object>());
    }

    public FieldConfigBuilder(FieldConfig borrowedFieldConfig) {
        this(borrowedFieldConfig.getName(), borrowedFieldConfig, new LinkedHashMap<FieldAttributeType, Object>(borrowedFieldConfig.getAttributeMap()));
    }

    public FieldConfigBuilder(String name, FieldConfig borrowedFieldConfig) {
        this(name, borrowedFieldConfig, new LinkedHashMap<FieldAttributeType, Object>(borrowedFieldConfig.getAttributeMap()));
    }

    public FieldConfigBuilder(String name, FieldConfig borrowedFieldConfig, Map<FieldAttributeType, Object> attributeMap) {
        this.name = name;
        this.borrowedFieldConfig = borrowedFieldConfig;
        this.attributeMap = attributeMap;
    }

    public String getName() {
        return name;
    }

    public FieldConfig getBorrowedFieldConfig() {
        return borrowedFieldConfig;
    }

    public FieldConfig build() {
        return new FieldConfig(name, borrowedFieldConfig, attributeMap);
    }

    private FieldConfigBuilder put(FieldAttributeType key, Object value) {
        attributeMap.put(key, value);
        return this;
    }

    public FieldConfigBuilder type(FieldType type) {
        return put(FieldAttributeType.TYPE, type);
    }

    public FieldConfigBuilder label(String label) {
        return put(FieldAttributeType.LABEL, label);
    }

    public FieldConfigBuilder draw(Draw draw) {
        return put(FieldAttributeType.DRAW, draw);
    }

    public FieldConfigBuilder min(Number n) {
        return put(FieldAttributeType.MIN, n);
    }

    public FieldConfigBuilder max(Number n) {
        return put(FieldAttributeType.MAX, n);
    }

}
