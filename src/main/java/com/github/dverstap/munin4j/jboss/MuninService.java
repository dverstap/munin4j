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

package com.github.dverstap.munin4j.jboss;

import com.github.dverstap.munin4j.core.GraphFinder;
import com.github.dverstap.munin4j.core.Server;
import com.github.dverstap.munin4j.jdk.JdkGraphFinder;
import org.jboss.system.ServiceMBeanSupport;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class MuninService extends ServiceMBeanSupport implements MuninServiceMBean {

    private String hostName;
    private String bindAddress;
    private int bindPort;

    private Server server;
    private Thread serverThread;

    @Override
    public String getHostName() {
        return hostName;
    }

    @Override
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Override
    public String getBindAddress() {
        return bindAddress;
    }

    @Override
    public void setBindAddress(String bindAddress) {
        this.bindAddress = bindAddress;
    }

    @Override
    public int getBindPort() {
        return bindPort;
    }

    @Override
    public void setBindPort(int bindPort) {
        this.bindPort = bindPort;
    }



    @Override
    public synchronized void startService() throws Exception {
        log.info("Starting munin service ...");

        if (server != null) {
            throw new IllegalStateException("Server is already running.");
        }

        if (hostName == null) {
            hostName = InetAddress.getLocalHost().getHostName();
        }

        server = new Server(hostName, createGraphFinders());
        if (bindAddress != null) {
            server.setBindAddress(bindAddress);
        }
        if (bindPort != 0) {
            server.setBindPort(bindPort);
        }

        // open TCP listen socket from startService() itself,
        // so deployment fails in case of trouble.
        server.start();

        serverThread = new Thread(server);
        serverThread.setName("munin4j");
        serverThread.start();

        log.info("munin server started successfully.");
    }

    protected List<GraphFinder> createGraphFinders() {
        List<GraphFinder> result = new ArrayList<GraphFinder>();
        result.add(new JdkGraphFinder());
        result.add(new JBossWebAppSessionsGraphFinder(getServer()));
        result.add(new JBossManagedConnectionPoolGraphFinder(getServer()));
        result.add(new GlobalRequestCountGraphFinder(getServer()));
        return result;
    }

    @Override
    public synchronized void stopService() {
        log.info("Stopping munin service ...");
        if (server == null) {
            throw new IllegalStateException("Munin server is not running.");
        }
        server.requestStop();
        try {
            log.info("Waiting for server thread to exit ...");
            serverThread.join();
        } catch (InterruptedException e) {
            log.warn("stop(): Interrupted while waiting for serverThread to exit.");
        } finally {
            serverThread = null;
            server = null;
        }
        log.info("Stopped successfully.");
    }
}
