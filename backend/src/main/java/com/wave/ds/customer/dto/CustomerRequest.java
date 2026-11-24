package com.wave.ds.customer.dto;

import com.wave.ds.customer.entity.Brand;
import com.wave.ds.customer.entity.SiteType;
import com.wave.ds.customer.entity.Zone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/** Customer induction form. Optional fields: email, alternateNumber, gemOrderNumber, oldCustomerId. */
public record CustomerRequest(

        @NotBlank(message = "Customer name is required")
        @Size(max = 200, message = "Customer name must be at most 200 characters")
        String customerName,

        @Email(message = "Enter a valid email address")
        @Size(max = 200, message = "Email must be at most 200 characters")
        String email,

        @NotBlank(message = "Contact number is required")
        @Pattern(regexp = "^\\d{10}$", message = "Contact number must be 10 digits")
        String contactNumber,

        @Pattern(regexp = "^(\\d{10})?$", message = "Alternate number must be 10 digits")
        String alternateNumber,

        @NotBlank(message = "Address is required")
        @Size(max = 500, message = "Address must be at most 500 characters")
        String address,

        @NotBlank(message = "City is required")
        @Size(max = 100, message = "City must be at most 100 characters")
        String city,

        @NotBlank(message = "Area/Sector is required")
        @Size(max = 150, message = "Area/Sector must be at most 150 characters")
        String areaSector,

        @NotNull(message = "Zone is required")
        Zone zone,

        @NotNull(message = "Site type is required")
        SiteType siteType,

        @NotBlank(message = "Pincode is required")
        @Pattern(regexp = "^\\d{6}$", message = "Pincode must be 6 digits")
        String pincode,

        @NotNull(message = "Customer type is required")
        Long customerTypeId,

        @NotNull(message = "Brand is required")
        Brand brand,

        @NotNull(message = "Company is required")
        Long companyId,

        @Size(max = 100, message = "GEM order must be at most 100 characters")
        String gemOrderNumber,

        @Size(max = 100, message = "Old customer id must be at most 100 characters")
        String oldCustomerId,

        @NotNull(message = "Date of start is required")
        LocalDate startDate) {
}
