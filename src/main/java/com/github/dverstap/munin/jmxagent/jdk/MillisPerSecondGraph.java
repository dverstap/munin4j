package com.github.dverstap.munin.jmxagent.jdk;

import com.github.dverstap.munin.jmxagent.framework.FieldConfig;
import com.github.dverstap.munin.jmxagent.framework.FieldConfigBuilder;
import com.github.dverstap.munin.jmxagent.framework.FieldType;
import com.github.dverstap.munin.jmxagent.framework.Graph;
import com.github.dverstap.munin.jmxagent.framework.GraphConfig;
import com.github.dverstap.munin.jmxagent.framework.GraphConfigBuilder;

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
