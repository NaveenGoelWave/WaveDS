package com.wave.ds.customer.repository;

import com.wave.ds.customer.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.domain.Specification;

public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    /** Loads customer type and company in the same query, avoiding N+1 selects on list pages. */
    @Override
    @EntityGraph(attributePaths = {"customerType", "company"})
    Page<Customer> findAll(Specification<Customer> spec, Pageable pageable);
}
