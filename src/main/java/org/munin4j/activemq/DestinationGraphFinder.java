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

import org.munin4j.core.Graph;
import org.munin4j.core.GraphFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class DestinationGraphFinder implements GraphFinder {

    private static final Logger log = LoggerFactory.getLogger(DestinationGraphFinder.class);

    private final MBeanServer mBeanServer;
    private final String type;
    private boolean monitorAdvisoryDestinations = false;

    public DestinationGraphFinder(MBeanServer mBeanServer, String type) {
        if (!("Queue".equals(type) || "Topic".equals(type))) {
            String msg = "Cannot monitor destinations of type " + type;
            if ("TempQueue".equals(type) || "TempTopic".equals(type)) {
                msg += ": each new temporary destination would create new RRD file, that would never be cleaned up, and fill up the munin database";
            }
            throw new IllegalArgumentException(msg);
        }
        this.mBeanServer = mBeanServer;
        this.type = type;
    }

    public boolean isMonitorAdvisoryDestinations() {
        return monitorAdvisoryDestinations;
    }

    public void setMonitorAdvisoryDestinations(boolean monitorAdvisoryDestinations) {
        this.monitorAdvisoryDestinations = monitorAdvisoryDestinations;
    }

    protected boolean shouldBeMonitored(String destinationName) {
        return monitorAdvisoryDestinations || !destinationName.startsWith("ActiveMQ.Advisory");
    }

    @Override
    public List<Graph> find() {
        List<Graph> result = new ArrayList<Graph>();
        try {
            Set<ObjectName> objectNames = mBeanServer.queryNames(new ObjectName("org.apache.activemq:type=Broker,destinationType=" + type + ",*"), new QueryExp() {
                @Override
                public boolean apply(ObjectName name) throws BadStringOperationException, BadBinaryOpValueExpException, BadAttributeValueExpException, InvalidApplicationException {
                    String mbeanName = name.toString();
                    return !(mbeanName.contains("endpoint") || mbeanName.contains("clientId"));
                }

                @Override
                public void setMBeanServer(MBeanServer s) {
                }
            });
            log.debug("Found {}", objectNames);
            for (ObjectName objectName : objectNames) {
                String destinationName = objectName.getKeyProperty("destinationName");
                if (shouldBeMonitored(destinationName)) {
                    String brokerName = objectName.getKeyProperty("brokerName");
                    String category = "ActiveMQ " + brokerName + " " + type + "s";
                    result.add(new DestinationMessageCountersGraph(mBeanServer, objectName, brokerName, destinationName, category));
                    result.add(new DestinationSizeGraph(mBeanServer, objectName, brokerName, destinationName, category));
                    result.add(new DestinationProducersAndConsumersGraph(mBeanServer, objectName, brokerName, destinationName, category));
                    result.add(new DestinationMemoryGraph(mBeanServer, objectName, brokerName, destinationName, category));
                    result.add(new DestinationTotalMemoryPortionGraph(mBeanServer, objectName, brokerName, destinationName, category));
                    result.add(new DestinationEnqueueTimeGraph(mBeanServer, objectName, brokerName, destinationName, category));
                }
            }
        } catch (MalformedObjectNameException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
