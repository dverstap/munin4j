package com.github.dverstap.munin.jmxagent.framework;

import java.util.LinkedHashMap;
import java.util.Map;

public class FieldConfigBuilder {

    private final String name;
    private final Map<FieldAttributeType, Object> attributeMap = new LinkedHashMap<FieldAttributeType, Object>();

    public FieldConfigBuilder(String name) {
        this.name = name;
    }

    public FieldConfig build() {
        return new FieldConfig(name, attributeMap);
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

}
