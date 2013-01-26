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

import java.util.LinkedHashMap;
import java.util.Map;

public class FieldConfigBuilder {

    private final String name;
    private final FieldConfig borrowedFieldConfig;
    private final Map<FieldAttributeType, Object> attributeMap;

    public FieldConfigBuilder(String name) {
        this(name, null, new LinkedHashMap<FieldAttributeType, Object>());
    }

    public FieldConfigBuilder(FieldConfig borrowedFieldConfig) {
        this(borrowedFieldConfig.getName(), borrowedFieldConfig, new LinkedHashMap<FieldAttributeType, Object>(borrowedFieldConfig.getAttributeMap()));
    }

    public FieldConfigBuilder(String name, FieldConfig borrowedFieldConfig) {
        this(name, borrowedFieldConfig, new LinkedHashMap<FieldAttributeType, Object>(borrowedFieldConfig.getAttributeMap()));
    }

    public FieldConfigBuilder(String name, FieldConfig borrowedFieldConfig, Map<FieldAttributeType, Object> attributeMap) {
        this.name = name;
        this.borrowedFieldConfig = borrowedFieldConfig;
        this.attributeMap = attributeMap;
    }

    public String getName() {
        return name;
    }

    public FieldConfig getBorrowedFieldConfig() {
        return borrowedFieldConfig;
    }

    public FieldConfig build() {
        return new FieldConfig(name, borrowedFieldConfig, attributeMap);
    }

    private FieldConfigBuilder put(FieldAttributeType key, Object value) {
        attributeMap.put(key, value);
        return this;
    }

    public FieldConfigBuilder type(FieldType type) {
        return put(FieldAttributeType.TYPE, type);
    }

    public FieldConfigBuilder label(String label) {
        return put(FieldAttributeType.LABEL, label);
    }

    public FieldConfigBuilder info(String info) {
        return put(FieldAttributeType.INFO, info);
    }

    public FieldConfigBuilder draw(Draw draw) {
        return put(FieldAttributeType.DRAW, draw);
    }

    public FieldConfigBuilder min(Number n) {
        return put(FieldAttributeType.MIN, n);
    }

    public FieldConfigBuilder max(Number n) {
        return put(FieldAttributeType.MAX, n);
    }

    public FieldConfigBuilder negative(String positive) {
        return put(FieldAttributeType.NEGATIVE, positive);
    }

    public FieldConfigBuilder graph(boolean on) {
        return put(FieldAttributeType.GRAPH, on ? "yes" : "no");
    }
}
