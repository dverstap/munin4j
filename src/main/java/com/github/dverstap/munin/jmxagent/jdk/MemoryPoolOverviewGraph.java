package com.github.dverstap.munin.jmxagent.jdk;

import com.github.dverstap.munin.jmxagent.framework.FieldConfig;
import com.github.dverstap.munin.jmxagent.framework.Graph;
import com.github.dverstap.munin.jmxagent.framework.GraphConfig;
import com.github.dverstap.munin.jmxagent.framework.GraphConfigBuilder;
import com.github.dverstap.munin.jmxagent.framework.GraphUtil;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MemoryPoolOverviewGraph implements Graph {

    private final List<FieldConfig> borrowedDataSources = new ArrayList<FieldConfig>();
    private final MemoryPoolField memoryPoolField;
    private final MemoryUsageField memoryUsageField;

    public MemoryPoolOverviewGraph(Map<MemoryPoolMXBean, Map<MemoryPoolField, DetailedMemoryPoolGraph>> map,
                                   MemoryPoolField memoryPoolField, MemoryUsageField memoryUsageField) {
        this.memoryPoolField = memoryPoolField;
        this.memoryUsageField = memoryUsageField;
        for (Map<MemoryPoolField, DetailedMemoryPoolGraph> graphMap : map.values()) {
            borrowedDataSources.add(graphMap.get(memoryPoolField).getField(memoryUsageField));
        }
    }

    @Override
    public GraphConfig buildConfig() {
        return new GraphConfigBuilder(GraphUtil.buildName(memoryPoolField.name() + "_" + memoryUsageField.name()))
                .title(memoryPoolField.getLabel() + ": " + memoryUsageField.getLabel())
                .vLabel("bytes")
                .category("Memory Pool Overviews")
                .args("--base 1024 -l 0")
                .info("As reported by the <a href='http://download.oracle.com/javase/1.5.0/docs/api/java/lang/management/MemoryPoolMXBean.html'>" + borrowedDataSources.size() + " MXBeans in the " + ManagementFactory.MEMORY_POOL_MXBEAN_DOMAIN_TYPE + " domain</a>.")
                .fields(borrowedDataSources)
                .build();
    }

    @Override
    public Map<FieldConfig, Object> fetchValues() {
        return Collections.emptyMap();
    }

}
