package com.github.dverstap.munin.jmxagent;

import java.util.Map;

public interface Graph {

    GraphConfig buildConfig();

    Map<FieldConfig, Object> fetchValues();

}
