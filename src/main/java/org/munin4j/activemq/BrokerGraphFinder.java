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

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BrokerGraphFinder implements GraphFinder {

    private static final Logger log = LoggerFactory.getLogger(BrokerGraphFinder.class);

    private final MBeanServer mBeanServer;

    public BrokerGraphFinder(MBeanServer mBeanServer) {
        this.mBeanServer = mBeanServer;
    }

    @Override
    public List<Graph> find() {
        List<Graph> result = new ArrayList<Graph>();
        try {
            Set<ObjectName> objectNames = mBeanServer.queryNames(new ObjectName("org.apache.activemq:Type=Broker,*"), null);
            log.debug("Found {}",objectNames);
            for (ObjectName objectName : objectNames) {
                String brokerName = objectName.getKeyProperty("BrokerName");
                String category = "ActiveMQ " + brokerName + " Broker";
                result.add(new BrokerMessageStoresLimitGraph(mBeanServer, objectName, brokerName, category));
                result.add(new BrokerMessageStoresUsageGraph(mBeanServer, objectName, brokerName, category));
                result.add(new BrokerDestinationsGraph(mBeanServer, objectName, brokerName, category));
                result.add(new BrokerSubscribersGraph(mBeanServer, objectName, brokerName, category));
                result.add(new BrokerMessageCountersGraph(mBeanServer, objectName, brokerName, category));
            }
        } catch (MalformedObjectNameException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
