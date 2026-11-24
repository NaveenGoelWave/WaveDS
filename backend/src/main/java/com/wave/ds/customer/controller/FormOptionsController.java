package com.wave.ds.customer.controller;

import com.wave.ds.customer.dto.FormOptions;
import com.wave.ds.customer.dto.FormOptions.EnumOption;
import com.wave.ds.customer.entity.Brand;
import com.wave.ds.customer.entity.SiteType;
import com.wave.ds.customer.entity.Zone;
import com.wave.ds.lookup.entity.LookupCategory;
import com.wave.ds.lookup.service.LookupService;
import java.util.Arrays;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/form-options")
public class FormOptionsController {

    private final LookupService lookupService;

    public FormOptionsController(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    @GetMapping
    public FormOptions options() {
        return new FormOptions(
                Arrays.stream(Zone.values()).map(z -> new EnumOption(z.name(), z.getLabel())).toList(),
                Arrays.stream(SiteType.values()).map(s -> new EnumOption(s.name(), s.getLabel())).toList(),
                Arrays.stream(Brand.values()).map(b -> new EnumOption(b.name(), b.getLabel())).toList(),
                lookupService.list(LookupCategory.CUSTOMER_TYPE),
                lookupService.list(LookupCategory.COMPANY));
    }
}
