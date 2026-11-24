package com.wave.ds.customer.entity;

public enum Zone {
    NORTH("North"),
    SOUTH("South"),
    EAST("East"),
    WEST("West"),
    CENTRAL("Central"),
    NORTH_EAST_DELHI("North East Delhi");

    private final String label;

    Zone(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
