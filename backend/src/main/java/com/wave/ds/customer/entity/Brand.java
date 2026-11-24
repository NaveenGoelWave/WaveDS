package com.wave.ds.customer.entity;

public enum Brand {
    WAVE("Wave"),
    DEER_PARK("Deer Park"),
    AQUAFINA("Aquafina"),
    DIVYA_JAL("Divya Jal (Patanjali)"),
    BISLERI("Bisleri"),
    CLEAR("Clear");

    private final String label;

    Brand(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
