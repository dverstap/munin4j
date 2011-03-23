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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;

public class Server implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(Server.class);

    private final String hostName;
    private String bindAddress = "0.0.0.0";
    private int bindPort = 14949;

    private final GraphFinder graphFinder;

    private volatile boolean stopped = false;
    private ServerSocket serverSocket;

    public Server(String hostName, List<GraphFinder> graphFinders) {
        this(hostName, new CompositeGraphFinder(graphFinders));
    }

    public Server(String hostName, GraphFinder graphFinder) {
        this.hostName = hostName;
        this.graphFinder = graphFinder;
    }

    public String getBindAddress() {
        return bindAddress;
    }

    public void setBindAddress(String bindAddress) {
        this.bindAddress = bindAddress;
    }

    public int getBindPort() {
        return bindPort;
    }

    public void setBindPort(int bindPort) {
        this.bindPort = bindPort;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(this.bindPort, 50, InetAddress.getByName(bindAddress));
    }

    public void run() {
        log.info("Starting munin node server on " + bindAddress + ":" + bindPort);

        stopped = false;
        while (!serverSocket.isClosed()) {
            Socket sock;
            try {
                sock = serverSocket.accept();
                handleConnection(sock); // does not throw any exceptions
            } catch (SocketException e) {
                if (stopped) {
                    log.debug("Got SocketException and stopped has been set to true; a clean stop must have been requested.");
                } else {
                    log.warn("Ignoring exception: " + e.getMessage(), e);
                }
            } catch (IOException e) {
                log.warn("Ignoring exception: " + e.getMessage(), e);
            }
        }
        log.debug("Munin server socket was closed.");
        log.info("Munin server has stopped listening for requests.");
        serverSocket = null;
    }

    private void handleConnection(Socket sock) {
        try {
            log.info("Accepted new connection from: " + sock.getInetAddress() + ":" + sock.getPort());
            sock.setSoTimeout(15000);
            sock.setTcpNoDelay(true);
            LineSocket lineSocket = new LineSocket(sock);
            Responder responder = new Responder(hostName, graphFinder.find(), lineSocket, lineSocket);
            responder.process();
        } catch (SocketTimeoutException ste) {
            log.warn("Closing connection from: " + sock.getInetAddress() + ":" + sock.getPort() + " because of timeout.");
        } catch (Throwable t) {
            log.warn(t.getMessage(), t);
        } finally {
            if (sock != null) {
                try {
                    log.info("Closing connection from: " + sock.getInetAddress() + ":" + sock.getPort());
                    sock.close();
                } catch (IOException e) {
                    log.warn("Failed to finally close socket because: " + e.getMessage(), e);
                }
            }
        }

    }

    public void requestStop() {
        log.info("Going to stop server ... ");
        this.stopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            log.warn("requestStop(): " + e.getMessage(), e);
        }
    }

}
