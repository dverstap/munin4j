/**
 * Copyright (c) 2011-2013 Davy Verstappen
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.munin4j.jdk;

import org.munin4j.core.FieldConfig;
import org.munin4j.core.FieldConfigBuilder;
import org.munin4j.core.Graph;
import org.munin4j.core.GraphConfig;
import org.munin4j.core.GraphConfigBuilder;
import org.munin4j.core.GraphUtil;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Map;

import static org.munin4j.core.Draw.AREA;
import static org.munin4j.core.FieldType.GAUGE;

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
                .category("JDK Threads")
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
