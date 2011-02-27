package com.github.dverstap.munin4j.jdk;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;

public class NonHeapMemoryGraph extends AbstractMemoryGraph {

    public NonHeapMemoryGraph() {
        super("nonheap", "Non-heap");
    }

    @Override
    protected MemoryUsage getMemoryUsage() {
        return ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
    }

}
