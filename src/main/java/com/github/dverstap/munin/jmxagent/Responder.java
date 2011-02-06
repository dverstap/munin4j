package com.github.dverstap.munin.jmxagent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

public class Responder {

    private static final Logger log = LoggerFactory.getLogger(Responder.class);
    
    private final String name;
    private final BufferedReader in;
    private final PrintStream out;

    private static int value = 0;

    public Responder(String name, BufferedReader in, PrintStream out) {
        this.name = name;
        this.in = in;
        this.out = out;
    }

    public void process() throws IOException {
        sendLine("# munin node at " + name);
        String line;
        while ((line = readLine()) != null) {
            if (line.startsWith("list")) {
                sendLine("ms");
            } else if (line.equals("config ms")) {
                sendLine("graph_title Milliseconds per second");
                sendLine("graph_vlabel ms/s");
                sendLine("ms.label ms");
                sendLine("ms.type COUNTER");
                sendLine(".");
            } else if (line.equals("fetch ms")) {
                value++;
                //sendLine("hello.value " + value);
                long v = System.currentTimeMillis();
                sendLine("ms.value " + v);
                sendLine(".");
// TODO handle "cap multigraph"
//            } else if (line.startsWith("cap")) {
//
            } else {
                log.warn("Received unknown command: " + line);
                // TODO print exact supported commands
                sendLine("# Unknown command. Try list, nodes, config, fetch, version or quit");
            }
        }
    }
    
    private String readLine() throws IOException {
        String line = in.readLine();
        if (line != null && log.isTraceEnabled()) {
            log.trace("Read line: " + line);
        }
        return line;
    }

    private void sendLine(String line) {
        log.trace("Send line: " + line);
        out.println(line);
    }
    
}
