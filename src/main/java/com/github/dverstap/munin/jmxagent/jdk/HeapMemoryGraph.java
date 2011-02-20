package com.github.dverstap.munin.jmxagent.jdk;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;

public class HeapMemoryGraph extends AbstractMemoryGraph {

    public HeapMemoryGraph() {
        super("heap", "Heap");
    }

    @Override
    protected MemoryUsage getMemoryUsage() {
        return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
    }

}
