package com.github.dverstap.munin.jmxagent.jdk;

import com.github.dverstap.munin.jmxagent.framework.FieldConfig;
import com.github.dverstap.munin.jmxagent.framework.FieldConfigBuilder;
import com.github.dverstap.munin.jmxagent.framework.Graph;
import com.github.dverstap.munin.jmxagent.framework.GraphConfig;
import com.github.dverstap.munin.jmxagent.framework.GraphConfigBuilder;
import com.github.dverstap.munin.jmxagent.framework.GraphUtil;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.dverstap.munin.jmxagent.framework.FieldType.GAUGE;

public abstract class AbstractMemoryPoolGraph implements Graph {

    private final Map<FieldConfig, MemoryPoolMXBean> memoryPoolMap = new LinkedHashMap<FieldConfig, MemoryPoolMXBean>();
    private final String postfix;

    public AbstractMemoryPoolGraph(String postfix) {
        this.postfix = postfix;
        for (MemoryPoolMXBean bean : ManagementFactory.getMemoryPoolMXBeans()) {
            memoryPoolMap.put(field(bean), bean);
        }
    }

    private FieldConfig field(MemoryPoolMXBean bean) {
        return new FieldConfigBuilder(GraphUtil.buildName("memorypool_" + bean.getName() + "_" + postfix))
                .label(bean.getName() + " " + postfix)
                .type(GAUGE)
                .build();
    }

    @Override
    public GraphConfig buildConfig() {
        return new GraphConfigBuilder(("memorypool_" + postfix).toLowerCase())
                .title("Memory Pool " + postfix)
                .vLabel("bytes")
                .category("Memory")
                .args("--base 1024 -l 0")
                .info("As reported by the <a href='http://download.oracle.com/javase/1.5.0/docs/api/java/lang/management/MemoryPoolMXBean.html'>" + memoryPoolMap.size() + " MXBeans in the " + ManagementFactory.MEMORY_POOL_MXBEAN_DOMAIN_TYPE + " domain</a>.")
                .fields(memoryPoolMap.keySet())
                .build();
    }

    @Override
    public Map<FieldConfig, Object> fetchValues() {
        Map<FieldConfig, Object> result = new LinkedHashMap<FieldConfig, Object>();
        for (Map.Entry<FieldConfig, MemoryPoolMXBean> entry : memoryPoolMap.entrySet()) {
            MemoryUsage memoryUsage = getMemoryUsage(entry.getValue());
            long tmp = -1;
            if (memoryUsage != null) {
                tmp = memoryUsage.getUsed();
            }
            result.put(entry.getKey(), tmp);
        }
        return result;
    }

    protected abstract MemoryUsage getMemoryUsage(MemoryPoolMXBean bean);

}
