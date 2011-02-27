package com.github.dverstap.munin4j.jdk;

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
