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

package org.munin4j.jmx;

import org.munin4j.core.Graph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;

public abstract class MBeanGraph implements Graph {

    protected final MBeanServer mBeanServer;
    protected final ObjectName objectName;

    public MBeanGraph(MBeanServer mBeanServer, ObjectName objectName) {
        this.mBeanServer = mBeanServer;
        this.objectName = objectName;
    }

    protected Object getAttribute(String name) {
        Logger log = LoggerFactory.getLogger(this.getClass());
        try {
            log.debug("Getting attribute {} from {}", name, objectName);
            return mBeanServer.getAttribute(objectName, name);
        } catch (Throwable t) {
            log.warn(t.getMessage(), t);
            return null;
        }
    }

    protected String getDescription(String name) {
        Logger log = LoggerFactory.getLogger(this.getClass());
        try {
            log.debug("Getting description of {} from {}", name, objectName);
            MBeanInfo mBeanInfo = mBeanServer.getMBeanInfo(objectName);
            return findAttributeInfo(mBeanInfo, name).getDescription();
        } catch (Throwable t) {
            log.warn(t.getMessage(), t);
            return null;
        }
    }

    private MBeanAttributeInfo findAttributeInfo(MBeanInfo mBeanInfo, String name) {
        for (MBeanAttributeInfo info : mBeanInfo.getAttributes()) {
            if (info.getName().equals(name)) {
                return info;
            }
        }
        throw new IllegalArgumentException(String.format("Could not find attribute '%s' in MBean '%s'", name, objectName));
    }

}
