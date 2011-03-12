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

package com.github.dverstap.munin4j.jboss;

import com.github.dverstap.munin4j.core.FieldConfig;
import com.github.dverstap.munin4j.core.FieldConfigBuilder;
import com.github.dverstap.munin4j.core.FieldType;
import com.github.dverstap.munin4j.core.GraphUtil;
import com.github.dverstap.munin4j.jmx.SimpleMBeanGraph;

import javax.management.MBeanServer;
import javax.management.ObjectName;

public class GlobalRequestCountTrafficGraph extends SimpleMBeanGraph {

    public GlobalRequestCountTrafficGraph(MBeanServer mBeanServer, ObjectName objectName) {
        super(mBeanServer, objectName,
                objectName.getKeyProperty("name") + " Traffic",
                "bytes in (-) / out (+) per ${graph_period}",
                "jboss.web GlobalRequestProcessor");
        FieldConfig receivedFieldConfig = new FieldConfigBuilder(GraphUtil.buildName("bytesReceived"))
                .label("Bytes Received")
                .type(FieldType.DERIVE)
                .min(0L)
                .graph(false)
                .build();
        fieldConfigAttributeMap.put(receivedFieldConfig, "bytesReceived");

        FieldConfig sentFieldConfig = new FieldConfigBuilder(GraphUtil.buildName("bytesSent"))
                .label("Bytes") // note that the label here is Bytes, not Bytes Sent
                .type(FieldType.DERIVE)
                .min(0L)
                .negative(receivedFieldConfig.getName())
                .build();
        fieldConfigAttributeMap.put(sentFieldConfig, "bytesSent");
    }

    @Override
    protected String buildGraphName() {
        return GraphUtil.buildName(objectName.toString()) + "_traffic";
    }

}
