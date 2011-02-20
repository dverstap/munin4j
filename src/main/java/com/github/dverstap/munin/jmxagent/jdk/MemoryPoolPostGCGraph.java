package com.github.dverstap.munin.jmxagent.jdk;

import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;

public class MemoryPoolPostGCGraph extends AbstractMemoryPoolGraph {

    public MemoryPoolPostGCGraph() {
        super("(Post)CollectionUsage");
    }

    @Override
    protected MemoryUsage getMemoryUsage(MemoryPoolMXBean bean) {
        return bean.getCollectionUsage();
    }

}
