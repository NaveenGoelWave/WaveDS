package com.wave.ds.customer.repository;

import com.wave.ds.customer.entity.Brand;
import com.wave.ds.customer.entity.Customer;
import com.wave.ds.customer.entity.Zone;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class CustomerSpecifications {

    private CustomerSpecifications() {
    }

    public static Specification<Customer> filter(String search, Zone zone, Brand brand) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (search != null && !search.isBlank()) {
                String like = "%" + search.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("customerName")), like),
                        cb.like(cb.lower(root.get("email")), like),
                        cb.like(root.get("contactNumber"), like),
                        cb.like(root.get("alternateNumber"), like),
                        cb.like(cb.lower(root.get("city")), like),
                        cb.like(cb.lower(root.get("areaSector")), like),
                        cb.like(root.get("pincode"), like),
                        cb.like(cb.lower(root.get("oldCustomerId")), like),
                        cb.like(cb.lower(root.get("gemOrderNumber")), like)));
            }
            if (zone != null) {
                predicates.add(cb.equal(root.get("zone"), zone));
            }
            if (brand != null) {
                predicates.add(cb.equal(root.get("brand"), brand));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
