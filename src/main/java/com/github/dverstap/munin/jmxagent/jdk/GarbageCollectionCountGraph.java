package com.github.dverstap.munin.jmxagent.jdk;

import java.lang.management.GarbageCollectorMXBean;

public class GarbageCollectionCountGraph extends AbstractGarbageCollectionGraph {

    public GarbageCollectionCountGraph() {
        super("Count", "gc/s");
    }

    @Override
    protected Long getDataPoint(GarbageCollectorMXBean bean) {
        return bean.getCollectionCount();
    }

}
