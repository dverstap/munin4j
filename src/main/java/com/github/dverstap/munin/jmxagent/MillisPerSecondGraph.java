package com.github.dverstap.munin.jmxagent;

import java.util.Collections;
import java.util.Map;

public class MillisPerSecondGraph implements Graph {

    private final FieldConfig millisPerSecond;

    public MillisPerSecondGraph() {
        millisPerSecond = new FieldConfigBuilder("ms")
                .type(FieldType.COUNTER)
                .label("ms")
                .build();
    }

    @Override
    public GraphConfig buildConfig() {
        return new GraphConfigBuilder("ms2")
                .title("Milliseconds per second 2")
                .vLabel("ms/s")
                .fields(millisPerSecond)
                .build();
    }

    @Override
    public Map<FieldConfig, Object> fetchValues() {
        return Collections.singletonMap(millisPerSecond, (Object) System.currentTimeMillis());
    }

}
