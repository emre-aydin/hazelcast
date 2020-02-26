package com.hazelcast.internal.management.dto;

public class DataStructure {
    private final String name;
    private final int type;

    public DataStructure(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }
}
