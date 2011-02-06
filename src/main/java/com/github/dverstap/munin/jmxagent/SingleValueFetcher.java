package com.github.dverstap.munin.jmxagent;

import java.util.Collections;
import java.util.Map;

public abstract class SingleValueFetcher implements Fetcher {

    private final String name;

    public SingleValueFetcher(String name) {
        this.name = name;
    }

    public Map<String, Object> fetchValues() {
        return Collections.singletonMap(name, fetchValue());
    }

    public abstract Object fetchValue();

}
