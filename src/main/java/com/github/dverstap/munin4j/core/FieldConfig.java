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

package com.github.dverstap.munin4j.core;

import java.util.Collections;
import java.util.Map;

// TODO rename to DataSourceConfig
public class FieldConfig {

    private final String name;
    private final FieldConfig borrowedFieldConfig;
    private final Map<FieldAttributeType, Object> attributeMap;
    private GraphConfig graphConfig;

    public FieldConfig(String name, FieldConfig borrowedFieldConfig, Map<FieldAttributeType, Object> attributeMap) {
        this.name = name;
        this.borrowedFieldConfig = borrowedFieldConfig;
        this.attributeMap = Collections.unmodifiableMap(attributeMap);
    }

    public String getName() {
        return name;
    }

    public FieldConfig getBorrowedFieldConfig() {
        return borrowedFieldConfig;
    }

    public void send(LineWriter out) {
        for (Map.Entry<FieldAttributeType, Object> entry : attributeMap.entrySet()) {
            out.writeLine(name + "." + entry.getKey().getMuninName() + " " + entry.getValue());
        }
    }

    public GraphConfig getGraphConfig() {
        return graphConfig;
    }

    public Map<FieldAttributeType, Object> getAttributeMap() {
        return attributeMap;
    }

    void setGraphConfig(GraphConfig graphConfig) {
        if (this.graphConfig != null) {
            throw new IllegalStateException("graphConfig cannot be modified once it is set.");
        }
        this.graphConfig = graphConfig;
    }
}
