package com.github.dverstap.munin4j.jdk;

import com.github.dverstap.munin4j.core.FieldConfig;
import com.github.dverstap.munin4j.core.FieldConfigBuilder;
import com.github.dverstap.munin4j.core.Graph;
import com.github.dverstap.munin4j.core.GraphConfig;
import com.github.dverstap.munin4j.core.GraphConfigBuilder;
import com.github.dverstap.munin4j.core.GraphUtil;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.dverstap.munin4j.core.FieldType.DERIVE;

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
                .type(DERIVE)
                .min(0L)
                .build();
    }


    @Override
    public GraphConfig buildConfig() {
        return new GraphConfigBuilder((prefix + "_" + postfix).toLowerCase())
                .title("Garbage Collection " + postfix)
                .vLabel(vLabel)
                .category("Garbage Collection")
                .info("As reported by the <a href='http://download.oracle.com/javase/1.5.0/docs/api/java/lang/management/GarbageCollectorMXBean.html'>" + garbageCollectorMap.size() + " MXBeans in the " + ManagementFactory.GARBAGE_COLLECTOR_MXBEAN_DOMAIN_TYPE + " domain</a>.")
                .fields(garbageCollectorMap.keySet())
                .build();
    }

    @Override
    public Map<FieldConfig, Long> fetchValues() {
        Map<FieldConfig, Long> map = new LinkedHashMap<FieldConfig, Long>();
        for (Map.Entry<FieldConfig, GarbageCollectorMXBean> entry : garbageCollectorMap.entrySet()) {
            map.put(entry.getKey(), getDataPoint(entry.getValue()));
        }
        return map;
    }

    protected abstract Long getDataPoint(GarbageCollectorMXBean bean);

}
