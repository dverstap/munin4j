package com.github.dverstap.munin.jmxagent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Server implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(Server.class);

    private String address = "0.0.0.0";
    private int port = 14949;

    private final Map<String, GraphConfig> graphConfigMap = new LinkedHashMap<String, GraphConfig>();
    private final Map<String, Graph> graphMap = new LinkedHashMap<String, Graph>();

    public Server(List<Graph> graphs) {
        for (Graph graph : graphs) {
            GraphConfig config = graph.buildConfig();
            graphConfigMap.put(config.getName(), config);
            graphMap.put(config.getName(), graph);
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void run() {
        log.info("Starting munin node server on " + address + ":" + port);

        ServerSocket serverSocket;
        Thread thread;


        try {
            serverSocket = new ServerSocket(this.port, 50, InetAddress.getByName(address));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        try {
//            serverSocket.setSoTimeout(1000);
//        } catch (SocketException e) {
//            throw new RuntimeException(e);
//        }

        // TODO support clean shutdown
        while (!Thread.interrupted()) {
            Socket sock = null;
            try {
                sock = serverSocket.accept();
                log.info("Accepted new connection from: " + sock.getInetAddress() + ":" + sock.getPort());
                sock.setSoTimeout(15000);
                LineSocket lineSocket = new LineSocket(sock);
                Responder responder = new Responder("myjava", graphConfigMap, graphMap, lineSocket, lineSocket);
                responder.process();
            } catch (SocketTimeoutException ste) {
                log.warn("Closing connection from: " + sock.getInetAddress() + ":" + sock.getPort() + " because of timeout.");
            } catch (IOException e) {
                log.warn(e.getMessage(), e);
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

        // TODO should be in a finally block:
        try {
            serverSocket.close();
        } catch (IOException e) {
            log.warn("Failed to finally close server socket because: " + e.getMessage(), e);
        }
    }

}
