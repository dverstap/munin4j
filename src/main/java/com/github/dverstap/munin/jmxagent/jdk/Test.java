package com.github.dverstap.munin.jmxagent.jdk;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryManagerMXBean;

public class Test {

    public static void main(String[] args) {
        for (MemoryManagerMXBean bean : ManagementFactory.getMemoryManagerMXBeans()) {
            for (MemoryPoolField memoryPoolField: MemoryPoolField.values()) {
                for (MemoryUsageField memoryUsageField : MemoryUsageField.values()) {
                    System.out.println(bean.getName() + ": " + memoryPoolField + ": " + memoryUsageField);
                }
            }
        }
    }
}
