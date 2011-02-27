package com.github.dverstap.munin4j.core;

import java.util.Map;

public interface Graph {

    GraphConfig buildConfig();

    Map<FieldConfig, ?> fetchValues();

}
