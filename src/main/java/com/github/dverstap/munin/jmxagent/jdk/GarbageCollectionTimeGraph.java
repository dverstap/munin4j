package com.github.dverstap.munin.jmxagent.jdk;

import java.lang.management.GarbageCollectorMXBean;

public class GarbageCollectionTimeGraph extends AbstractGarbageCollectionGraph {

    public GarbageCollectionTimeGraph() {
        super("Time", "ms/s");
    }

    @Override
    protected Long getDataPoint(GarbageCollectorMXBean bean) {
        return bean.getCollectionTime();
    }

}
