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

package com.github.dverstap.munin4j.jdk;

import com.github.dverstap.munin4j.core.Graph;
import com.github.dverstap.munin4j.core.Server;

import java.lang.management.MemoryPoolMXBean;
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
                new GarbageCollectionTimeGraph()
        ));

        Map<MemoryPoolMXBean, Map<MemoryPoolField, DetailedMemoryPoolGraph>> memoryPoolMap = DetailedMemoryPoolGraph.build();
        for (Map<MemoryPoolField, DetailedMemoryPoolGraph> map : memoryPoolMap.values()) {
            for (DetailedMemoryPoolGraph graph : map.values()) {
                graphs.add(graph);
            }
        }
        for (MemoryPoolField memoryPoolField : MemoryPoolField.values()) {
            for (MemoryUsageField memoryUsageField : MemoryUsageField.values()) {
                graphs.add(new MemoryPoolOverviewGraph(memoryPoolMap, memoryPoolField, memoryUsageField));
            }
        }

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