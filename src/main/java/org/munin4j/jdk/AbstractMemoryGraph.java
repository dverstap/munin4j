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
import org.munin4j.core.FieldType;
import org.munin4j.core.Graph;
import org.munin4j.core.GraphConfig;
import org.munin4j.core.GraphConfigBuilder;

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
                .category("JDK Memory Totals")
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
