package com.github.dverstap.munin.jmxagent;

import java.io.IOException;

public interface LineReader {

    String readLine() throws IOException;

}
