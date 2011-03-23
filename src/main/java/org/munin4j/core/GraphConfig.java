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

package org.munin4j.core;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GraphConfig {

    private final String name;
    private final Map<GraphAttributeType, Object> attributeMap;
    private final List<FieldConfig> fieldConfigs; // TODO make this a Map again and check uniqueness on put

    public GraphConfig(String name, Map<GraphAttributeType, Object> attributeMap, List<FieldConfig> fieldConfigs) {
        this.name = name;

        boolean hasBorrowedDataSources = false;
        for (FieldConfig field : fieldConfigs) {
            field.setGraphConfig(this);
            if (field.getBorrowedFieldConfig() != null) {
                hasBorrowedDataSources = true;
            }
        }

        this.fieldConfigs = Collections.unmodifiableList(fieldConfigs);
        if (hasBorrowedDataSources && !attributeMap.containsKey(GraphAttributeType.GRAPH_ORDER)) {
            attributeMap.put(GraphAttributeType.GRAPH_ORDER, buildGraphOrder());
        }
        this.attributeMap = Collections.unmodifiableMap(attributeMap);
    }

    private String buildGraphOrder() {
        StringBuilder result = new StringBuilder();
        for (FieldConfig fieldConfig : fieldConfigs) {
            if (fieldConfig.getBorrowedFieldConfig() == null) {
                result.append(fieldConfig.getName());
            } else {
                result.append(fieldConfig.getName())
                        .append("=")
                        .append(fieldConfig.getBorrowedFieldConfig().getGraphConfig().getName())
                        .append(".")
                        .append(fieldConfig.getBorrowedFieldConfig().getName());
            }
            result.append(" ");
        }
        return result.toString();
    }

    public String getName() {
        return name;
    }

    public void send(LineWriter out) {
        for (Map.Entry<GraphAttributeType, Object> entry : attributeMap.entrySet()) {
            out.writeLine(entry.getKey().getMuninName() + " " + entry.getValue());
        }
        for (FieldConfig fieldConfig : fieldConfigs) {
            fieldConfig.send(out);
        }
        out.writeLine(".");
    }

}
