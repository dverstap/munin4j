package com.github.dverstap.munin.jmxagent.framework;

import java.io.IOException;

public interface LineReader {

    String readLine() throws IOException;

}
