package com.wave.ds.customer.mapper;

import com.wave.ds.customer.dto.CustomerRequest;
import com.wave.ds.customer.dto.CustomerResponse;
import com.wave.ds.customer.entity.Customer;
import com.wave.ds.lookup.entity.LookupValue;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    /** Copies form values onto an entity (used for both create and update). */
    public void apply(CustomerRequest r, Customer c, LookupValue customerType, LookupValue company) {
        c.setCustomerName(r.customerName().trim());
        c.setEmail(blankToNull(r.email()));
        c.setContactNumber(r.contactNumber().trim());
        c.setAlternateNumber(blankToNull(r.alternateNumber()));
        c.setAddress(r.address().trim());
        c.setCity(r.city().trim());
        c.setAreaSector(r.areaSector().trim());
        c.setZone(r.zone());
        c.setSiteType(r.siteType());
        c.setPincode(r.pincode().trim());
        c.setCustomerType(customerType);
        c.setBrand(r.brand());
        c.setCompany(company);
        c.setGemOrderNumber(blankToNull(r.gemOrderNumber()));
        c.setOldCustomerId(blankToNull(r.oldCustomerId()));
        c.setStartDate(r.startDate());
    }

    public CustomerResponse toResponse(Customer c) {
        return new CustomerResponse(
                c.getId(),
                c.getCustomerName(),
                c.getEmail(),
                c.getContactNumber(),
                c.getAlternateNumber(),
                c.getAddress(),
                c.getCity(),
                c.getAreaSector(),
                c.getZone(),
                c.getZone().getLabel(),
                c.getSiteType(),
                c.getSiteType().getLabel(),
                c.getPincode(),
                c.getCustomerType().getId(),
                c.getCustomerType().getLabel(),
                c.getBrand(),
                c.getBrand().getLabel(),
                c.getCompany().getId(),
                c.getCompany().getLabel(),
                c.getGemOrderNumber(),
                c.getOldCustomerId(),
                c.getStartDate(),
                c.getCreatedAt(),
                c.getUpdatedAt());
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
