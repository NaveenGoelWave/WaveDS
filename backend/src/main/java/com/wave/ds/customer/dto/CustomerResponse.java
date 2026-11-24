package com.wave.ds.customer.dto;

import com.wave.ds.customer.entity.Brand;
import com.wave.ds.customer.entity.SiteType;
import com.wave.ds.customer.entity.Zone;
import java.time.Instant;
import java.time.LocalDate;

public record CustomerResponse(
        Long id,
        String customerName,
        String email,
        String contactNumber,
        String alternateNumber,
        String address,
        String city,
        String areaSector,
        Zone zone,
        String zoneLabel,
        SiteType siteType,
        String siteTypeLabel,
        String pincode,
        Long customerTypeId,
        String customerType,
        Brand brand,
        String brandLabel,
        Long companyId,
        String company,
        String gemOrderNumber,
        String oldCustomerId,
        LocalDate startDate,
        Instant createdAt,
        Instant updatedAt) {
}
