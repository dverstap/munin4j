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

public class GraphUtil {

    public static String buildName(String str) {
        str = str.toLowerCase();
        StringBuilder result = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (Character.isJavaIdentifierPart(ch)) {
                result.append(ch);
            } else {
                result.append("_");
            }
        }
        return result.toString();
    }

    public static Map<FieldConfig, Object> build(FieldConfig key1, Object value1) {
        Map<FieldConfig, Object> result = new LinkedHashMap<FieldConfig, Object>();
        result.put(key1, value1);
        return result;
    }

    public static Map<FieldConfig, Object> build(FieldConfig key1, Object value1,
                                                 FieldConfig key2, Object value2) {
        Map<FieldConfig, Object> result = new LinkedHashMap<FieldConfig, Object>();
        result.put(key1, value1);
        result.put(key2, value2);
        return result;
    }

    public static Map<FieldConfig, Object> build(FieldConfig key1, Object value1,
                                                 FieldConfig key2, Object value2,
                                                 FieldConfig key3, Object value3) {
        Map<FieldConfig, Object> result = new LinkedHashMap<FieldConfig, Object>();
        result.put(key1, value1);
        result.put(key2, value2);
        result.put(key3, value3);
        return result;
    }

    public static Map<FieldConfig, Object> build(FieldConfig key1, Object value1,
                                                 FieldConfig key2, Object value2,
                                                 FieldConfig key3, Object value3,
                                                 FieldConfig key4, Object value4) {
        Map<FieldConfig, Object> result = new LinkedHashMap<FieldConfig, Object>();
        result.put(key1, value1);
        result.put(key2, value2);
        result.put(key3, value3);
        result.put(key4, value4);
        return result;
    }

    public static Map<FieldConfig, Object> build(FieldConfig key1, Object value1,
                                                 FieldConfig key2, Object value2,
                                                 FieldConfig key3, Object value3,
                                                 FieldConfig key4, Object value4,
                                                 FieldConfig key5, Object value5) {
        Map<FieldConfig, Object> result = new LinkedHashMap<FieldConfig, Object>();
        result.put(key1, value1);
        result.put(key2, value2);
        result.put(key3, value3);
        result.put(key4, value4);
        result.put(key5, value5);
        return result;
    }
}
