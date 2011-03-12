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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Responder {

    private static final Logger log = LoggerFactory.getLogger(Responder.class);

    private final String name;
    private final Map<String, GraphConfig> graphConfigMap = new LinkedHashMap<String, GraphConfig>();
    private final Map<String, Graph> graphMap = new LinkedHashMap<String, Graph>();
    private final LineReader in;
    private final LineWriter out;

    public Responder(String name, List<Graph> graphs, LineReader in, LineWriter out) {
        this.name = name;
        for (Graph graph : graphs) {
            GraphConfig config = graph.buildConfig();
            graphConfigMap.put(config.getName(), config);
            graphMap.put(config.getName(), graph);
        }
        this.in = in;
        this.out = out;
    }

    public void process() throws IOException {
        writeLine("# munin node at " + name);
        String line;
        while ((line = readLine()) != null) {
            if (line.startsWith("list")) {
                list();
            } else if (line.startsWith("config ")) {
                String graphName = line.substring("config ".length());
                config(graphName);
            } else if (line.startsWith("fetch ")) {
                String graphName = line.substring("fetch ".length());
                fetch(graphName);
// TODO handle "cap multigraph"
//            } else if (line.startsWith("cap")) {
//
            } else if (line.trim().equals("quit")) {
                return;
            } else if (line.startsWith("cap ")) {
                writeLine("# no additional capabilities supported.");
            } else {
                log.warn("Received unknown command: " + line);
                // TODO print exact supported commands
                writeLine("# Unknown command. Try list, nodes, config, fetch, version or quit");
            }
        }
    }


    private void list() {
        StringBuilder b = new StringBuilder();
        for (GraphConfig graphConfig : graphConfigMap.values()) {
            b.append(graphConfig.getName())
                    .append(" ");
        }
        writeLine(b.toString());
    }

    private void config(String graphName) {
        GraphConfig config = graphConfigMap.get(graphName);
        if (config != null) {
            config.send(out);
        } else {
            writeLine("# Unknown graph " + graphName);
        }

    }

    private void fetch(String graphName) {
        Graph graph = graphMap.get(graphName);
        if (graph != null) {
            Map<FieldConfig, ?> values = graph.fetchValues();
            validate(values);
            for (Map.Entry<FieldConfig, ?> entry : values.entrySet()) {
                writeLine(entry.getKey().getName() + ".value " + entry.getValue());
            }
            writeLine(".");
        } else {
            writeLine("# Unknown graph " + graphName);
        }
    }

    private void validate(Map<FieldConfig, ?> values) {
        for (Map.Entry<FieldConfig, ?> entry : values.entrySet()) {
            FieldConfig fieldConfig = entry.getKey();
            // TODO counter/derive must always return integer
        }
    }

    private String readLine() throws IOException {
        return in.readLine();
    }

    private void writeLine(String line) {
        out.writeLine(line);
    }

}
