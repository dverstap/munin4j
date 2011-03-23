/**
 * Copyright (c) 2011 Davy Verstappen
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

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.munin4j.core.FieldType.DERIVE;

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
                .category("JDK Garbage Collection")
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
