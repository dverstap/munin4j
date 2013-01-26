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

package org.munin4j.activemq;

import org.munin4j.core.*;
import org.munin4j.jmx.SimpleMBeanGraph;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.util.Map;

public class DestinationMemoryGraph extends SimpleMBeanGraph {

    private final FieldConfig memoryLimit;
    private final FieldConfig memoryUsage;
    private final FieldConfig cursorMemoryUsage;

    public DestinationMemoryGraph(MBeanServer mBeanServer, ObjectName objectName, String brokerName, String destinationName, String category) {
        super(mBeanServer, objectName, brokerName + " " + destinationName + " Memory", "bytes", category);
        memoryLimit = new FieldConfigBuilder(GraphUtil.buildName("MemoryLimit"))
                .label("Limit")
                .type(FieldType.GAUGE)
                .info(getDescription("MemoryLimit"))
                .build();
        memoryUsage = new FieldConfigBuilder(GraphUtil.buildName("MemoryUsage"))
                .label("Usage")
                .info("The amount of the memory limit used")
                .type(FieldType.GAUGE)
                .build();
        if (objectName.getKeyProperty("Type").equals("Queue")) {
            // CursorMemoryUsage
            cursorMemoryUsage = new FieldConfigBuilder(GraphUtil.buildName("CursorMemoryUsage"))
                    .label("Cursor Usage")
                    .info("The amount of the memory limit used by the cursor")  // not entirely sure ...
                    .type(FieldType.GAUGE)
                    .build();
        } else {
            cursorMemoryUsage = null;
        }
    }

    @Override
    public GraphConfig buildConfig() {
        return graphConfigBuilder()
                .base1024()
                .lowerLimit(0)
                .fields(memoryLimit, memoryUsage)
                .build();
    }

    @Override
    public Map<FieldConfig, Object> fetchValues() {
        Long memoryLimitValue = (Long) getAttribute("MemoryLimit");
        Integer memoryPercentUsageValue = (Integer) getAttribute("MemoryPercentUsage");
        Map<FieldConfig, Object> result = GraphUtil.build(memoryLimit, memoryLimitValue,
                memoryUsage, memoryLimitValue * memoryPercentUsageValue / 100);
        if (cursorMemoryUsage != null) {
            result.put(cursorMemoryUsage, getAttribute("CursorMemoryUsage"));
        }
        return result;
    }

    @Override
    protected String buildGraphName() {
        return super.buildGraphName() + "_memory";
    }
}
