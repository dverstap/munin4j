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

import static com.github.dverstap.munin4j.core.Draw.AREA;
import static com.github.dverstap.munin4j.core.FieldType.GAUGE;

public class LiveThreadsGraph implements Graph {

    private final FieldConfig threadCount;
    private final FieldConfig daemonThreadCount;
    private final FieldConfig peakThreadCount;

    public LiveThreadsGraph() {
        threadCount = new FieldConfigBuilder("thread_count")
                .label("Thread Count")
                .type(GAUGE)
                .draw(AREA)
                .build();
        daemonThreadCount = new FieldConfigBuilder("daemon_thread_count")
                .label("Deamon Thread Count")
                .type(GAUGE)
                .build();
        peakThreadCount = new FieldConfigBuilder("peak_thread_count")
                .label("Peak Thread Count")
                .type(GAUGE)
                .build();
    }

    @Override
    public GraphConfig buildConfig() {
        return new GraphConfigBuilder("live_threads")
                .title("Live Threads")
                .vLabel("threads")
                .category("Threads")
                .info("As reported by <a href='http://download.oracle.com/javase/1.5.0/docs/api/java/lang/management/ThreadMXBean.html'>" + ManagementFactory.THREAD_MXBEAN_NAME + "</a>.")
                .fields(threadCount, daemonThreadCount, peakThreadCount)
                .build();
    }

    @Override
    public Map<FieldConfig, Object> fetchValues() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return GraphUtil.build(threadCount, bean.getThreadCount(),
                daemonThreadCount, bean.getDaemonThreadCount(),
                peakThreadCount, bean.getPeakThreadCount());
    }
}
