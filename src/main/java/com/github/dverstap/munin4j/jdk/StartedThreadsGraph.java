package com.github.dverstap.munin4j.jdk;

import com.github.dverstap.munin4j.core.FieldConfig;
import com.github.dverstap.munin4j.core.FieldConfigBuilder;
import com.github.dverstap.munin4j.core.Graph;
import com.github.dverstap.munin4j.core.GraphConfig;
import com.github.dverstap.munin4j.core.GraphConfigBuilder;
import com.github.dverstap.munin4j.core.GraphUtil;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Map;

import static com.github.dverstap.munin4j.core.FieldType.DERIVE;

public class StartedThreadsGraph implements Graph {

    private final FieldConfig totalStartedThreadsCount;

    public StartedThreadsGraph() {
        totalStartedThreadsCount = new FieldConfigBuilder("total_started_threads_count")
                .label("Total Started Threads Count")
                .type(DERIVE)
                .min(0L)
                .build();
    }

    @Override
    public GraphConfig buildConfig() {
        return new GraphConfigBuilder("started_threads_count")
                .title("Started Threads")
                .vLabel("threads started/s")
                .category("Threads")
                .info("As reported by <a href='http://download.oracle.com/javase/1.5.0/docs/api/java/lang/management/ThreadMXBean.html'>" + ManagementFactory.THREAD_MXBEAN_NAME + "</a>.")
                .fields(totalStartedThreadsCount)
                .build();
    }

    @Override
    public Map<FieldConfig, Object> fetchValues() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return GraphUtil.build(totalStartedThreadsCount, bean.getTotalStartedThreadCount());
    }
}
