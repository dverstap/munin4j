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

import org.munin4j.jmx.GaugeMBeanGraph;

import javax.management.MBeanServer;
import javax.management.ObjectName;

public class DestinationEnqueueTimeGraph extends GaugeMBeanGraph {


    public DestinationEnqueueTimeGraph(MBeanServer mBeanServer, ObjectName objectName, String brokerName, String destinationName, String category) {
        super(mBeanServer, objectName, brokerName + " " + destinationName + " Enqueue Time", "sec", category);
        add("MinEnqueueTime", "Min");
        add("AverageEnqueueTime", "Average");
        add("MaxEnqueueTime", "Max");
    }

    @Override
    protected Double getAttribute(String name) {
        Object valueInMillis = super.getAttribute(name);
        if (valueInMillis instanceof Double) {
            return ((Double) valueInMillis) / 1000.0;
        } else {
            return ((Long) valueInMillis) / 1000.0;
        }
    }

    @Override
    protected String buildGraphName() {
        return super.buildGraphName() + "_enqueuetime";
    }
}