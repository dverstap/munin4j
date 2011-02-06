package com.github.dverstap.munin.jmxagent;

import java.util.Map;

public interface Fetcher {

    Map<String, Object> fetchValues();

}
