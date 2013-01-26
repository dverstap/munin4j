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

package org.munin4j.core;

import java.util.*;

public class GraphConfigBuilder {

    private final String name;
    private final Map<GraphAttributeType, Object> attributeMap = new LinkedHashMap<GraphAttributeType, Object>();
    private final List<FieldConfig> fieldConfigs = new ArrayList<FieldConfig>();

    public GraphConfigBuilder(String name) {
        this.name = name;
    }

    public GraphConfig build() {
        return new GraphConfig(name, attributeMap, fieldConfigs);
    }

    private GraphConfigBuilder put(GraphAttributeType key, Object value) {
        attributeMap.put(key, value);
        return this;
    }

    public GraphConfigBuilder fields(FieldConfig... fields) {
        fieldConfigs.addAll(Arrays.asList(fields));
        return this;
    }

    public GraphConfigBuilder fields(Collection<FieldConfig> fields) {
        fieldConfigs.addAll(fields);
        return this;
    }

    public GraphConfigBuilder title(String title) {
        return put(GraphAttributeType.GRAPH_TITLE, title);
    }

    public GraphConfigBuilder vLabel(String vLabel) {
        return put(GraphAttributeType.GRAPH_VLABEL, vLabel);
    }
    public GraphConfigBuilder category(String category) {
        return put(GraphAttributeType.GRAPH_CATEGORY, category);
    }

    public GraphConfigBuilder info(String info) {
        return put(GraphAttributeType.GRAPH_INFO, info);
    }

    public GraphConfigBuilder args(String args) {
        return put(GraphAttributeType.GRAPH_ARGS, args);
    }

    public GraphConfigBuilder lowerLimit(Number limit) {
        return addGraphArg("--lower-limit " + limit);
    }

    public GraphConfigBuilder upperLimit(Number limit) {
        return addGraphArg("--upper-limit " + limit);
    }

    public GraphConfigBuilder base1000() {
        return addGraphArg("--base 1000");
    }

    public GraphConfigBuilder base1024() {
        return addGraphArg("--base 1024");
    }

    public GraphConfigBuilder addGraphArg(String arg) {
        String args = (String) attributeMap.get(GraphAttributeType.GRAPH_ARGS);
        if (args == null) {
            args = "";
        }
        args += " " + arg;
        attributeMap.put(GraphAttributeType.GRAPH_ARGS, args);
        return this;
    }
}
