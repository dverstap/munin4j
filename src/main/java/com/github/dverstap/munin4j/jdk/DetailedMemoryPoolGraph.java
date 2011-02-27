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

package com.github.dverstap.munin4j.jdk;

import com.github.dverstap.munin4j.core.FieldConfig;
import com.github.dverstap.munin4j.core.FieldConfigBuilder;
import com.github.dverstap.munin4j.core.Graph;
import com.github.dverstap.munin4j.core.GraphConfig;
import com.github.dverstap.munin4j.core.GraphConfigBuilder;
import com.github.dverstap.munin4j.core.GraphUtil;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.dverstap.munin4j.core.FieldType.GAUGE;

public class DetailedMemoryPoolGraph implements Graph {

    private final MemoryPoolMXBean bean;
    private final MemoryPoolField memoryPoolField;

    private final Map<MemoryUsageField, FieldConfig> fieldConfigMap = new EnumMap<MemoryUsageField, FieldConfig>(MemoryUsageField.class);

    public DetailedMemoryPoolGraph(MemoryPoolMXBean bean, MemoryPoolField memoryPoolField) {
        this.bean = bean;
        this.memoryPoolField = memoryPoolField;
        for (MemoryUsageField memoryUsageField : MemoryUsageField.values()) {
            fieldConfigMap.put(memoryUsageField, buildField(memoryUsageField));
        }
    }

    private FieldConfig buildField(MemoryUsageField field) {
        return new FieldConfigBuilder(GraphUtil.buildName(field.name()))
                .label(field.getLabel())
                .type(GAUGE)
                .build();
    }

    private String buildGraphName() {
        return GraphUtil.buildName(bean.getName() + "_" + memoryPoolField.name());
    }

    public FieldConfig getField(MemoryUsageField memoryUsageField) {
        return fieldConfigMap.get(memoryUsageField);
    }

    @Override
    public GraphConfig buildConfig() {
        return new GraphConfigBuilder(buildGraphName())
                .title(bean.getName() + ": " + memoryPoolField.getLabel())
                .vLabel("bytes")
                .category("Memory Pool: " + bean.getName())
                .args("--base 1024 -l 0")
                .info("As reported by <a href='http://download.oracle.com/javase/1.5.0/docs/api/java/lang/management/MemoryPoolMXBean.html\\#" + memoryPoolField.getMethodName() + "()'>MemoryPoolMXBean." + memoryPoolField.getMethodName() + "()</a>.")
                .fields(fieldConfigMap.values())
                .build();
    }

    @Override
    public Map<FieldConfig, Object> fetchValues() {
        MemoryUsage memoryUsage = getMemoryUsage();
        Map<FieldConfig, Object> result = new LinkedHashMap<FieldConfig, Object>();
        for (Map.Entry<MemoryUsageField, FieldConfig> entry : fieldConfigMap.entrySet()) {
            long tmp = -1;
            if (memoryUsage != null) {
                tmp = getValue(memoryUsage, entry.getKey());
            }
            result.put(entry.getValue(), tmp);
        }
        return result;
    }

    private MemoryUsage getMemoryUsage() {
        switch (memoryPoolField) {
            case USAGE:
                return bean.getUsage();
            case PEAK_USAGE:
                return bean.getPeakUsage();
            case COLLECTION_USAGE:
                return bean.getCollectionUsage();
            default:
                throw new IllegalStateException("Unknown MemoryPoolField: " + memoryPoolField);
        }
    }
    
    private long getValue(MemoryUsage memoryUsage, MemoryUsageField field) {
        switch (field) {
            case INIT:
                return memoryUsage.getInit();
            case USED:
                return memoryUsage.getUsed();
            case COMMITTED:
                return memoryUsage.getCommitted();
            case MAX:
                return memoryUsage.getMax();
            default:
                throw new IllegalStateException("Unknown MemoryUsageField: " + field);
            
        }
    }

    public static Map<MemoryPoolMXBean, Map<MemoryPoolField, DetailedMemoryPoolGraph>> build() {
        Map<MemoryPoolMXBean, Map<MemoryPoolField, DetailedMemoryPoolGraph>> result = new LinkedHashMap<MemoryPoolMXBean, Map<MemoryPoolField, DetailedMemoryPoolGraph>>();
        for (MemoryPoolMXBean bean : ManagementFactory.getMemoryPoolMXBeans()) {
            Map<MemoryPoolField, DetailedMemoryPoolGraph> map = new EnumMap<MemoryPoolField, DetailedMemoryPoolGraph>(MemoryPoolField.class);
            for (MemoryPoolField memoryPoolField : MemoryPoolField.values()) {
                map.put(memoryPoolField, new DetailedMemoryPoolGraph(bean, memoryPoolField));
            }
            result.put(bean, map);
        }
        return result;
    }

}
