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
                .category("JDK Memory Pool Overviews: " + memoryPoolField.getLabel())
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
