package com.wave.ds.lookup.dto;

import com.wave.ds.lookup.entity.LookupCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LookupCreateRequest(
        @NotNull(message = "Category is required") LookupCategory category,
        @NotBlank(message = "Label is required")
        @Size(max = 150, message = "Label must be at most 150 characters") String label) {
}
