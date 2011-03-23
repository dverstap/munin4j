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

public class JBossWebAppHistoricalSessionsGraph extends MBeanGraph {

    private final FieldConfig createdSessions;
    private final FieldConfig expiredSessions;
    private final FieldConfig rejectedSessions;

    public JBossWebAppHistoricalSessionsGraph(MBeanServer mBeanServer, ObjectName objectName) {
        super(mBeanServer, objectName);
        createdSessions = new FieldConfigBuilder("created_sessions")
                .label("Created Sessions")
                .type(FieldType.DERIVE)
                .min(0L)
                .build();
        expiredSessions = new FieldConfigBuilder("expired_sessions")
                .label("Expired Sessions")
                .type(FieldType.DERIVE)
                .min(0L)
                .build();
        rejectedSessions = new FieldConfigBuilder("rejected_sessions")
                .label("Rejected Sessions")
                .type(FieldType.DERIVE)
                .min(0L)
                .build();
    }

    @Override
    public GraphConfig buildConfig() {
        return new GraphConfigBuilder(this.buildGraphName())
                .title(objectName.getKeyProperty("path") + " Historical Sessions")
                .vLabel("sessions")
                .category("jboss.web")
                .fields(createdSessions, expiredSessions, rejectedSessions)
                .build();
    }

    private String buildGraphName() {
        String path = objectName.getKeyProperty("path");
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (path.equals("")) {
            path = "root";
        }
        return GraphUtil.buildName(path + "_historical_sessions");
    }

    @Override
    public Map<FieldConfig, Object> fetchValues() {
        Map<FieldConfig, Object> result = new LinkedHashMap<FieldConfig, Object>();
        result.put(createdSessions, getAttribute("sessionCounter"));
        result.put(expiredSessions, getAttribute("expiredSessions"));
        result.put(rejectedSessions, getAttribute("rejectedSessions"));
        return result;
    }

}
