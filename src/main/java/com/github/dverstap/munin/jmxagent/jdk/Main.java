package com.github.dverstap.munin.jmxagent.jdk;

import com.github.dverstap.munin.jmxagent.framework.Graph;
import com.github.dverstap.munin.jmxagent.framework.Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        new Thread(new ThreadStarter()).start();

        List<Graph> graphs = new ArrayList<Graph>(Arrays.asList(
                new MillisPerSecondGraph(),
                new HeapMemoryGraph(),
                new NonHeapMemoryGraph(),
                new LiveThreadsGraph(),
                new StartedThreadsGraph(),
                new GarbageCollectionCountGraph(),
                new GarbageCollectionTimeGraph(),
                new MemoryPoolUsageGraph(),
                new MemoryPoolPostGCGraph()
        ));

        for (Map<MemoryPoolField, DetailedMemoryPoolGraph> map : DetailedMemoryPoolGraph.build().values()) {
            for (DetailedMemoryPoolGraph graph : map.values()) {
                graphs.add(graph);
            }
        }

//        for (GarbageCollectorMXBean bean : ManagementFactory.getGarbageCollectorMXBeans()) {
//            graphs.add(new AbstractGarbageCollectionGraph(bean));
//        }

        new Server(graphs).run();
    }

}

class ThreadStarter implements Runnable {

    @Override
    public void run() {
        for (; ;) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // ignored
            }
            new Thread() {

                @Override
                public void run() {
                    for (int i = 0; i < 100; i++) {
                        byte[] ints = new byte[1024 * 1024];
                    }
                    //System.out.println("ALLOCATED MEM");
                }

            }.start();
        }
    }
}