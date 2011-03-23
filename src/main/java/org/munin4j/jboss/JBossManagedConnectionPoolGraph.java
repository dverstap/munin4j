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

public class JBossManagedConnectionPoolGraph extends MBeanGraph {

    private final FieldConfig minSize;
    private final FieldConfig maxSize;
    private final FieldConfig currentConnections;
    private final FieldConfig currentConnectionsInUse;
    private final FieldConfig peekConnectionsInUse;


    public JBossManagedConnectionPoolGraph(MBeanServer mBeanServer, ObjectName objectName) {
        super(mBeanServer, objectName);
        minSize = new FieldConfigBuilder("min_size")
                .label("Min Size")
                .type(FieldType.GAUGE)
                .build();
        maxSize = new FieldConfigBuilder("max_size")
                .label("Max Size")
                .type(FieldType.GAUGE)
                .build();
        currentConnections = new FieldConfigBuilder("current_connections")
                .label("Current Connections")
                .type(FieldType.GAUGE)
                .build();
        currentConnectionsInUse = new FieldConfigBuilder("current_connections_in_use")
                .label("Current Connections in Use")
                .type(FieldType.GAUGE)
                .build();
        peekConnectionsInUse = new FieldConfigBuilder("peek_connections_in_use")
                .label("Peek Connections in Use")
                .type(FieldType.GAUGE)
                .build();

    }

    @Override
    public GraphConfig buildConfig() {
        return new GraphConfigBuilder(this.buildGraphName())
                .title(objectName.getKeyProperty("name") + " Connection Pool")
                .vLabel("connections")
                .category("jboss.jca")
                .fields(minSize, maxSize, currentConnections, currentConnectionsInUse, peekConnectionsInUse)
                .build();
    }

    private String buildGraphName() {
        String name = objectName.getKeyProperty("name");
        return GraphUtil.buildName(name + "_connection_pool");
    }

    @Override
    public Map<FieldConfig, Object> fetchValues() {
        Map<FieldConfig, Object> result = new LinkedHashMap<FieldConfig, Object>();
        result.put(minSize, getAttribute("MinSize"));
        result.put(maxSize, getAttribute("MaxSize"));
        result.put(currentConnections, getAttribute("ConnectionCount"));
        result.put(currentConnectionsInUse, getAttribute("InUseConnectionCount"));
        result.put(peekConnectionsInUse, getAttribute("MaxConnectionsInUseCount"));
        return result;
    }


}
