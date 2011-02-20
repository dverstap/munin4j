package com.github.dverstap.munin.jmxagent.jdk;

import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;

public class MemoryPoolUsageGraph extends AbstractMemoryPoolGraph {

    public MemoryPoolUsageGraph() {
        super("Usage");
    }

    @Override
    protected MemoryUsage getMemoryUsage(MemoryPoolMXBean bean) {
        return bean.getUsage();
    }

}
