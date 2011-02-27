package com.github.dverstap.munin4j.core;

public enum FieldType {

    COUNTER,
    DERIVE, // often used instead of counter, with min=0, to deal better with counter resets
    ABSOLUTE,
    GAUGE

}
