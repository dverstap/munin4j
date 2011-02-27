package com.github.dverstap.munin4j.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class Responder {

    private static final Logger log = LoggerFactory.getLogger(Responder.class);

    private final String name;
    private final Map<String, GraphConfig> graphConfigMap;
    private final Map<String, Graph> graphMap;
    private final LineReader in;
    private final LineWriter out;

    public Responder(String name, Map<String, GraphConfig> graphConfigMap, Map<String, Graph> graphMap, LineReader in, LineWriter out) {
        this.name = name;
        this.graphConfigMap = graphConfigMap;
        this.graphMap = graphMap;
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
