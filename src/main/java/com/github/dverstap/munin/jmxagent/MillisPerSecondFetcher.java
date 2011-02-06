package com.github.dverstap.munin.jmxagent;

public class MillisPerSecondFetcher extends SingleValueFetcher {

    public MillisPerSecondFetcher() {
        super("ms");
    }

    @Override
    public Object fetchValue() {
        return System.currentTimeMillis();
    }

}
