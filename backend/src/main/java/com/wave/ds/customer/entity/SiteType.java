package com.wave.ds.customer.entity;

public enum SiteType {
    SINGLE_SITE_POINT("Single-site point"),
    MULTI_SITE_POINT("Multi-site point");

    private final String label;

    SiteType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
