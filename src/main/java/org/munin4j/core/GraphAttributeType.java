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

// http://munin-monitoring.org/wiki/protocol-config
public enum GraphAttributeType {

    GRAPH_TITLE("graph_title"),
    CREATE_ARGS("create_args"),

    GRAPH_VLABEL("graph_vlabel"),
    GRAPH_SCALE("graph_scale"),
    GRAPH_CATEGORY("graph_category"),
    GRAPH_ARGS("graph_args"),
    GRAPH_INFO("graph_info"),
    GRAPH_PERIOD("graph_period"),
    GRAPH_ORDER("graph_order"),
    GRAPH_PRINTF("graph_printf");

    private final String muninName;

    GraphAttributeType(String name) {
        muninName = name;
    }

    public String getMuninName() {
        return muninName;
    }


}
