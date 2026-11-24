package com.wave.ds.customer.dto;

import com.wave.ds.lookup.dto.LookupOption;
import java.util.List;

/** Everything the customer form needs to fill its dropdowns, in one call. */
public record FormOptions(
        List<EnumOption> zones,
        List<EnumOption> siteTypes,
        List<EnumOption> brands,
        List<LookupOption> customerTypes,
        List<LookupOption> companies) {

    public record EnumOption(String value, String label) {
    }
}
