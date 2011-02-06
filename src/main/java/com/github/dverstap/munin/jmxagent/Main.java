package com.github.dverstap.munin.jmxagent;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Graph> graphs = Arrays.<Graph>asList(new MillisPerSecondGraph());
        new Server(graphs).run();
    }

}
