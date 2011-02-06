package com.github.dverstap.munin.jmxagent;

import java.util.LinkedHashMap;
import java.util.Map;

public class FieldConfigBuilder {

    private final String name;
    private final Map<FieldAttributeType, Object> attributeMap = new LinkedHashMap<FieldAttributeType, Object>();

    public FieldConfigBuilder(String name) {
        this.name = name;
    }

    public FieldConfigBuilder type(FieldType type) {
        attributeMap.put(FieldAttributeType.TYPE, type);
        return this;
    }

    public FieldConfigBuilder label(String label) {
        attributeMap.put(FieldAttributeType.LABEL, label);
        return this;
    }

    public FieldConfig build() {
        return new FieldConfig(name, attributeMap);
    }

}
