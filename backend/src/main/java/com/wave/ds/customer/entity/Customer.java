package com.wave.ds.customer.entity;

import com.wave.ds.common.audit.BaseEntity;
import com.wave.ds.lookup.entity.LookupValue;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "customer")
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name", nullable = false, length = 200)
    private String customerName;

    @Column(length = 200)
    private String email;

    @Column(name = "contact_number", nullable = false, length = 15)
    private String contactNumber;

    @Column(name = "alternate_number", length = 15)
    private String alternateNumber;

    @Column(nullable = false, length = 500)
    private String address;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(name = "area_sector", nullable = false, length = 150)
    private String areaSector;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Zone zone;

    @Enumerated(EnumType.STRING)
    @Column(name = "site_type", nullable = false, length = 30)
    private SiteType siteType;

    @Column(nullable = false, length = 6)
    private String pincode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_type_id", nullable = false)
    private LookupValue customerType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private LookupValue company;

    @Column(name = "gem_order_number", length = 100)
    private String gemOrderNumber;

    @Column(name = "old_customer_id", length = 100)
    private String oldCustomerId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    public Long getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAlternateNumber() {
        return alternateNumber;
    }

    public void setAlternateNumber(String alternateNumber) {
        this.alternateNumber = alternateNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAreaSector() {
        return areaSector;
    }

    public void setAreaSector(String areaSector) {
        this.areaSector = areaSector;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public SiteType getSiteType() {
        return siteType;
    }

    public void setSiteType(SiteType siteType) {
        this.siteType = siteType;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public LookupValue getCustomerType() {
        return customerType;
    }

    public void setCustomerType(LookupValue customerType) {
        this.customerType = customerType;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public LookupValue getCompany() {
        return company;
    }

    public void setCompany(LookupValue company) {
        this.company = company;
    }

    public String getGemOrderNumber() {
        return gemOrderNumber;
    }

    public void setGemOrderNumber(String gemOrderNumber) {
        this.gemOrderNumber = gemOrderNumber;
    }

    public String getOldCustomerId() {
        return oldCustomerId;
    }

    public void setOldCustomerId(String oldCustomerId) {
        this.oldCustomerId = oldCustomerId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
}
