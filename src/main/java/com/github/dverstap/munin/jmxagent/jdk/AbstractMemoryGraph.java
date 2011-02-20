package com.github.dverstap.munin.jmxagent.jdk;

import com.github.dverstap.munin.jmxagent.framework.FieldConfig;
import com.github.dverstap.munin.jmxagent.framework.FieldConfigBuilder;
import com.github.dverstap.munin.jmxagent.framework.FieldType;
import com.github.dverstap.munin.jmxagent.framework.Graph;
import com.github.dverstap.munin.jmxagent.framework.GraphConfig;
import com.github.dverstap.munin.jmxagent.framework.GraphConfigBuilder;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractMemoryGraph implements Graph {

    private final String name;
    private final String title;

    private final FieldConfig init;
    private final FieldConfig used;
    private final FieldConfig committed;
    private final FieldConfig max;


    public AbstractMemoryGraph(String name, String title) {
        this.name = name;
        this.title = title;
        init = field("Init");
        used = field("Used");
        committed = field("Committed");
        max = field("Max");

    }

    private FieldConfig field(String title) {
        return new FieldConfigBuilder(title.toLowerCase())
                .label(title)
                .type(FieldType.GAUGE)
                .build();
    }

    protected abstract MemoryUsage getMemoryUsage();

    @Override
    public GraphConfig buildConfig() {
        return new GraphConfigBuilder(name)
                .title(title)
                .vLabel("bytes")
                .category("Memory Totals")
                .args("--base 1024 -l 0")
                .info("As reported by <a href='http://download.oracle.com/javase/1.5.0/docs/api/java/lang/management/MemoryMXBean.html'>" + ManagementFactory.MEMORY_MXBEAN_NAME + "</a>")
                .fields(init, used, committed, max)
                .build();
    }

    @Override
    public Map<FieldConfig, Object> fetchValues() {
        Map<FieldConfig, Object> map = new LinkedHashMap<FieldConfig, java.lang.Object>();
        MemoryUsage usage = getMemoryUsage();
        map.put(init, usage.getInit());
        map.put(used, usage.getUsed());
        map.put(committed, usage.getCommitted());
        map.put(max, usage.getMax());
        return map;
    }

}
