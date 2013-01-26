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
import org.munin4j.core.FieldConfigBuilder;
import org.munin4j.core.FieldType;
import org.munin4j.core.GraphConfig;
import org.munin4j.core.GraphConfigBuilder;
import org.munin4j.core.GraphUtil;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleMBeanGraph extends MBeanGraph {

    protected final Map<FieldConfig, String> fieldConfigAttributeMap = new LinkedHashMap<FieldConfig, String>();
    protected final String title;
    protected final String vLabel;
    protected final String category;

    public SimpleMBeanGraph(MBeanServer mBeanServer, ObjectName objectName, String title, String vLabel, String category) {
        super(mBeanServer, objectName);
        this.title = title;
        this.vLabel = vLabel;
        this.category = category;
    }

    public FieldConfig add(String mBeanAttributeName, String label, FieldType type) {
        FieldConfig fieldConfig = fieldConfigBuilder(mBeanAttributeName, label, type).build();
        fieldConfigAttributeMap.put(fieldConfig, mBeanAttributeName);
        return fieldConfig;
    }

    protected FieldConfigBuilder fieldConfigBuilder(String mBeanAttributeName, String label, FieldType type) {
        FieldConfigBuilder builder = new FieldConfigBuilder(GraphUtil.buildName(mBeanAttributeName))
                .label(label)
                .type(type);
        String description = getDescription(mBeanAttributeName);
        if (description != null) {
            builder.info(description);
        }
        return builder;
    }

    @Override
    public GraphConfig buildConfig() {
        return graphConfigBuilder().build();
    }

    protected GraphConfigBuilder graphConfigBuilder() {
        return new GraphConfigBuilder(buildGraphName())
                .title(title)
                .vLabel(vLabel)
                .category(category)
                .fields(fieldConfigAttributeMap.keySet());
    }

    protected String buildGraphName() {
        return GraphUtil.buildName(objectName.toString());
    }

    @Override
    public Map<FieldConfig, Object> fetchValues() {
        Map<FieldConfig,Object> result = new LinkedHashMap<FieldConfig, Object>();
        for (Map.Entry<FieldConfig, String> entry : fieldConfigAttributeMap.entrySet()) {
            result.put(entry.getKey(), getAttribute(entry.getValue()));
        }
        return result;
    }

}
