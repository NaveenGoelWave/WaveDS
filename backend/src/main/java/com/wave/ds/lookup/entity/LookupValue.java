package com.wave.ds.lookup.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "lookup_value")
public class LookupValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private LookupCategory category;

    @Column(nullable = false, length = 150)
    private String label;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    protected LookupValue() {
    }

    public LookupValue(LookupCategory category, String label, int sortOrder) {
        this.category = category;
        this.label = label;
        this.sortOrder = sortOrder;
    }

    public Long getId() {
        return id;
    }

    public LookupCategory getCategory() {
        return category;
    }

    public String getLabel() {
        return label;
    }

    public boolean isActive() {
        return active;
    }

    public int getSortOrder() {
        return sortOrder;
    }
}
