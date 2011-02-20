package com.github.dverstap.munin.jmxagent.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class LineSocket implements LineReader, LineWriter {

    private static final Logger log = LoggerFactory.getLogger(LineSocket.class);

    private final BufferedReader in;
    private final PrintStream out;

    public LineSocket(Socket socket) throws IOException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintStream(socket.getOutputStream());
    }

    @Override
    public void writeLine(String line) {
        log.trace("Send line: " + line);
        out.println(line);
    }

    @Override
    public String readLine() throws IOException {
        String line = in.readLine();
        if (line != null && log.isTraceEnabled()) {
            log.trace("Read line: " + line);
        }
        return line;
    }

}
