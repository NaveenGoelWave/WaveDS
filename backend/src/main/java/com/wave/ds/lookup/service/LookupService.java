package com.wave.ds.lookup.service;

import com.wave.ds.common.exception.BadRequestException;
import com.wave.ds.common.exception.ConflictException;
import com.wave.ds.lookup.dto.LookupCreateRequest;
import com.wave.ds.lookup.dto.LookupOption;
import com.wave.ds.lookup.entity.LookupCategory;
import com.wave.ds.lookup.entity.LookupValue;
import com.wave.ds.lookup.repository.LookupValueRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LookupService {

    private final LookupValueRepository repository;

    public LookupService(LookupValueRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<LookupOption> list(LookupCategory category) {
        return repository.findByCategoryAndActiveTrueOrderBySortOrderAscLabelAsc(category).stream()
                .map(v -> new LookupOption(v.getId(), v.getLabel()))
                .toList();
    }

    @Transactional
    public LookupOption create(LookupCreateRequest request) {
        String label = request.label().trim();
        if (repository.existsByCategoryAndLabelIgnoreCase(request.category(), label)) {
            throw new ConflictException(request.category() + " '" + label + "' already exists");
        }
        int nextOrder = (int) repository.countByCategory(request.category()) + 1;
        LookupValue saved = repository.save(new LookupValue(request.category(), label, nextOrder));
        return new LookupOption(saved.getId(), saved.getLabel());
    }

    /** Used by other features to resolve a dropdown selection; rejects unknown or inactive ids. */
    @Transactional(readOnly = true)
    public LookupValue requireActive(Long id, LookupCategory category) {
        return repository.findByIdAndCategoryAndActiveTrue(id, category)
                .orElseThrow(() -> new BadRequestException("Invalid " + category + " id: " + id));
    }
}
