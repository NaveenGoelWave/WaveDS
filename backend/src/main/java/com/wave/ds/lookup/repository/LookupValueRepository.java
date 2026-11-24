package com.wave.ds.lookup.repository;

import com.wave.ds.lookup.entity.LookupCategory;
import com.wave.ds.lookup.entity.LookupValue;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LookupValueRepository extends JpaRepository<LookupValue, Long> {

    List<LookupValue> findByCategoryAndActiveTrueOrderBySortOrderAscLabelAsc(LookupCategory category);

    Optional<LookupValue> findByIdAndCategoryAndActiveTrue(Long id, LookupCategory category);

    boolean existsByCategoryAndLabelIgnoreCase(LookupCategory category, String label);

    long countByCategory(LookupCategory category);
}
