package com.wave.ds.customer.service;

import com.wave.ds.customer.dto.CustomerRequest;
import com.wave.ds.customer.dto.CustomerResponse;
import com.wave.ds.customer.entity.Brand;
import com.wave.ds.customer.entity.Customer;
import com.wave.ds.customer.entity.Zone;
import com.wave.ds.customer.exception.CustomerNotFoundException;
import com.wave.ds.customer.mapper.CustomerMapper;
import com.wave.ds.customer.repository.CustomerRepository;
import com.wave.ds.customer.repository.CustomerSpecifications;
import com.wave.ds.lookup.entity.LookupCategory;
import com.wave.ds.lookup.entity.LookupValue;
import com.wave.ds.lookup.service.LookupService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final LookupService lookupService;
    private final CustomerMapper mapper;

    public CustomerServiceImpl(CustomerRepository repository, LookupService lookupService, CustomerMapper mapper) {
        this.repository = repository;
        this.lookupService = lookupService;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponse> search(String search, Zone zone, Brand brand, Pageable pageable) {
        return repository.findAll(CustomerSpecifications.filter(search, zone, brand), pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse get(Long id) {
        return mapper.toResponse(find(id));
    }

    @Override
    @Transactional
    public CustomerResponse create(CustomerRequest request) {
        Customer customer = new Customer();
        populate(customer, request);
        return mapper.toResponse(repository.save(customer));
    }

    @Override
    @Transactional
    public CustomerResponse update(Long id, CustomerRequest request) {
        Customer customer = find(id);
        populate(customer, request);
        return mapper.toResponse(repository.saveAndFlush(customer));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.delete(find(id));
    }

    private void populate(Customer customer, CustomerRequest request) {
        LookupValue customerType = lookupService.requireActive(request.customerTypeId(), LookupCategory.CUSTOMER_TYPE);
        LookupValue company = lookupService.requireActive(request.companyId(), LookupCategory.COMPANY);
        mapper.apply(request, customer, customerType, company);
    }

    private Customer find(Long id) {
        return repository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
    }
}
