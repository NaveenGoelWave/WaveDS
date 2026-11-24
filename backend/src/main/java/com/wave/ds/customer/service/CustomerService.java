package com.wave.ds.customer.service;

import com.wave.ds.customer.dto.CustomerRequest;
import com.wave.ds.customer.dto.CustomerResponse;
import com.wave.ds.customer.entity.Brand;
import com.wave.ds.customer.entity.Zone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    Page<CustomerResponse> search(String search, Zone zone, Brand brand, Pageable pageable);

    CustomerResponse get(Long id);

    CustomerResponse create(CustomerRequest request);

    CustomerResponse update(Long id, CustomerRequest request);

    void delete(Long id);
}
