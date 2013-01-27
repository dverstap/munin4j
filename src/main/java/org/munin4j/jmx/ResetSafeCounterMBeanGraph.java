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

import org.munin4j.core.FieldConfig;
import org.munin4j.core.FieldType;

import javax.management.MBeanServer;
import javax.management.ObjectName;

public class ResetSafeCounterMBeanGraph extends SimpleMBeanGraph {

    public ResetSafeCounterMBeanGraph(MBeanServer mBeanServer, ObjectName objectName, String title, String vLabel, String category) {
        super(mBeanServer, objectName, title, vLabel, category);
    }

    public FieldConfig add(String mBeanAttributeName, String label) {
        FieldConfig fieldConfig = fieldConfigBuilder(mBeanAttributeName, label, FieldType.DERIVE)
                .min(0L)
                .build();
        fieldConfigAttributeMap.put(fieldConfig, mBeanAttributeName);
        return fieldConfig;
    }

}