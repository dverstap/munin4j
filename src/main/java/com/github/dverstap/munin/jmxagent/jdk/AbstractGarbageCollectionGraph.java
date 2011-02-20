package com.github.dverstap.munin.jmxagent.jdk;

import com.github.dverstap.munin.jmxagent.framework.FieldConfig;
import com.github.dverstap.munin.jmxagent.framework.FieldConfigBuilder;
import com.github.dverstap.munin.jmxagent.framework.Graph;
import com.github.dverstap.munin.jmxagent.framework.GraphConfig;
import com.github.dverstap.munin.jmxagent.framework.GraphConfigBuilder;
import com.github.dverstap.munin.jmxagent.framework.GraphUtil;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.dverstap.munin.jmxagent.framework.FieldType.COUNTER;

public abstract class AbstractGarbageCollectionGraph implements Graph {

    private final Map<FieldConfig, GarbageCollectorMXBean> garbageCollectorMap;

    private final String prefix = "GC";
    private final String postfix;
    private final String vLabel;

    public AbstractGarbageCollectionGraph(String postfix, String vLabel) {
        this.postfix = postfix;
        this.vLabel = vLabel;
        Map<FieldConfig, GarbageCollectorMXBean> map = new LinkedHashMap<FieldConfig, GarbageCollectorMXBean>();
        for (GarbageCollectorMXBean bean : ManagementFactory.getGarbageCollectorMXBeans()) {
            map.put(field(bean), bean);
        }
        garbageCollectorMap = Collections.unmodifiableMap(map);
    }

    private FieldConfig field(GarbageCollectorMXBean bean) {
        return new FieldConfigBuilder(GraphUtil.buildName(prefix + "_" + bean.getName() + "_" + postfix))
                .label(bean.getName())
                .type(COUNTER)
                .build();
    }


    @Override
    public GraphConfig buildConfig() {
        return new GraphConfigBuilder((prefix + "_" + postfix).toLowerCase())
                .title("Garbage Collection " + postfix)
                .vLabel(vLabel)
                .category("Memory")
                .info("As reported by the <a href='http://download.oracle.com/javase/1.5.0/docs/api/java/lang/management/GarbageCollectorMXBean.html'>" + garbageCollectorMap.size() + " MXBeans in the " + ManagementFactory.GARBAGE_COLLECTOR_MXBEAN_DOMAIN_TYPE + " domain</a>.")
                .fields(garbageCollectorMap.keySet())
                .build();
    }

    @Override
    public Map<FieldConfig, Object> fetchValues() {
        Map<FieldConfig, Object> map = new LinkedHashMap<FieldConfig, Object>();
        for (Map.Entry<FieldConfig, GarbageCollectorMXBean> entry : garbageCollectorMap.entrySet()) {
            map.put(entry.getKey(), getDataPoint(entry.getValue()));
        }
        return map;
    }

    protected abstract Object getDataPoint(GarbageCollectorMXBean bean);

}
