package com.github.dverstap.munin4j.jdk;

import com.github.dverstap.munin4j.core.FieldConfig;
import com.github.dverstap.munin4j.core.FieldConfigBuilder;
import com.github.dverstap.munin4j.core.FieldType;
import com.github.dverstap.munin4j.core.Graph;
import com.github.dverstap.munin4j.core.GraphConfig;
import com.github.dverstap.munin4j.core.GraphConfigBuilder;

import java.util.Collections;
import java.util.Map;

public class MillisPerSecondGraph implements Graph {

    private final FieldConfig millisPerSecond;

    public MillisPerSecondGraph() {
        millisPerSecond = new FieldConfigBuilder("ms")
                .type(FieldType.DERIVE)
                .label("ms")
                .min(0L)
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
