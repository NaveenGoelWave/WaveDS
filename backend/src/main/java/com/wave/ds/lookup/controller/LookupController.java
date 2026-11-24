package com.wave.ds.lookup.controller;

import com.wave.ds.lookup.dto.LookupCreateRequest;
import com.wave.ds.lookup.dto.LookupOption;
import com.wave.ds.lookup.entity.LookupCategory;
import com.wave.ds.lookup.service.LookupService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lookups")
public class LookupController {

    private final LookupService service;

    public LookupController(LookupService service) {
        this.service = service;
    }

    @GetMapping("/{category}")
    public List<LookupOption> list(@PathVariable LookupCategory category) {
        return service.list(category);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LookupOption create(@Valid @RequestBody LookupCreateRequest request) {
        return service.create(request);
    }
}
