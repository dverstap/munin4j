package com.github.dverstap.munin4j.jdk;

import com.github.dverstap.munin4j.core.FieldConfig;
import com.github.dverstap.munin4j.core.FieldConfigBuilder;
import com.github.dverstap.munin4j.core.Graph;
import com.github.dverstap.munin4j.core.GraphConfig;
import com.github.dverstap.munin4j.core.GraphConfigBuilder;
import com.github.dverstap.munin4j.core.GraphUtil;

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
        for (Map.Entry<MemoryPoolMXBean, Map<MemoryPoolField, DetailedMemoryPoolGraph>> entry : map.entrySet()) {
            borrowedDataSources.add(buildField(entry.getKey(), entry.getValue()));
        }
    }

    private FieldConfig buildField(MemoryPoolMXBean bean, Map<MemoryPoolField, DetailedMemoryPoolGraph> graphMap) {
        FieldConfig borrowedFieldConfig = graphMap.get(memoryPoolField).getField(memoryUsageField);
        return new FieldConfigBuilder(GraphUtil.buildName(bean.getName()), borrowedFieldConfig)
                .label(bean.getName())
                .build();
    }

    @Override
    public GraphConfig buildConfig() {
        return new GraphConfigBuilder(GraphUtil.buildName(memoryPoolField.name() + "_" + memoryUsageField.name()))
                .title(memoryPoolField.getLabel() + ": " + memoryUsageField.getLabel())
                .vLabel("bytes")
                .category("Memory Pool Overviews: " + memoryPoolField.getLabel())
                .args("--base 1024 -l 0")
                .info("As reported by the <a href='http://download.oracle.com/javase/1.5.0/docs/api/java/lang/management/MemoryPoolMXBean.html'>" + borrowedDataSources.size() + " MXBeans in the " + ManagementFactory.MEMORY_POOL_MXBEAN_DOMAIN_TYPE + " domain</a>.")
                .fields(borrowedDataSources)
                .build();
    }

    @Override
    public Map<FieldConfig, Object> fetchValues() {
        // we only have borrowed datasources, so nothing to fetch
        return Collections.emptyMap();
    }

}
