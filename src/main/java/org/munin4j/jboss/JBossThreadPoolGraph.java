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

package org.munin4j.jboss;

import org.munin4j.core.FieldConfig;
import org.munin4j.core.FieldConfigBuilder;
import org.munin4j.core.FieldType;
import org.munin4j.core.GraphConfig;
import org.munin4j.core.GraphConfigBuilder;
import org.munin4j.core.GraphUtil;
import org.munin4j.jmx.MBeanGraph;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.util.LinkedHashMap;
import java.util.Map;

public class JBossThreadPoolGraph extends MBeanGraph {

    private final FieldConfig minSpareThreads;
    private final FieldConfig maxSpareThreads;
    private final FieldConfig maxThreads;
    private final FieldConfig currentThreads;
    private final FieldConfig currentThreadsInUse;


    public JBossThreadPoolGraph(MBeanServer mBeanServer, ObjectName objectName) {
        super(mBeanServer, objectName);
        minSpareThreads = new FieldConfigBuilder("min_spare_threads")
                .label("Min Spare Threads")
                .type(FieldType.GAUGE)
                .build();
        maxSpareThreads = new FieldConfigBuilder("max_spare_threads")
                .label("Max Spare Threads")
                .type(FieldType.GAUGE)
                .build();
        maxThreads = new FieldConfigBuilder("max_threads")
                .label("Max Threads")
                .type(FieldType.GAUGE)
                .build();
        currentThreads = new FieldConfigBuilder("current_threads")
                .label("Current Threads")
                .type(FieldType.GAUGE)
                .build();
        currentThreadsInUse = new FieldConfigBuilder("current_threads_in_use")
                .label("Current Threads in Use")
                .type(FieldType.GAUGE)
                .build();
    }

    @Override
    public GraphConfig buildConfig() {
        return new GraphConfigBuilder(this.buildGraphName())
                .title(objectName.getKeyProperty("name") + " Thread Pool")
                .vLabel("threads")
                .category("jboss.web")
                .fields(minSpareThreads, maxSpareThreads, maxThreads, currentThreads, currentThreadsInUse)
                .build();
    }

    private String buildGraphName() {
        String name = objectName.getKeyProperty("name");
        return GraphUtil.buildName(name + "_thread_pool");
    }

    @Override
    public Map<FieldConfig, Object> fetchValues() {
        Map<FieldConfig, Object> result = new LinkedHashMap<FieldConfig, Object>();
        result.put(minSpareThreads, getAttribute("minSpareThreads"));
        result.put(maxSpareThreads, getAttribute("maxSpareThreads"));
        result.put(maxThreads, getAttribute("maxThreads"));
        result.put(currentThreads, getAttribute("currentThreadCount"));
        result.put(currentThreadsInUse, getAttribute("currentThreadsBusy"));
        return result;
    }


}
