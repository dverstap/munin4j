package com.github.dverstap.munin.jmxagent.framework;

import java.util.Map;

public interface Graph {

    GraphConfig buildConfig();

    Map<FieldConfig, ?> fetchValues();

}
