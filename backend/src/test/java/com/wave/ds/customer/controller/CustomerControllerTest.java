package com.wave.ds.customer.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wave.ds.customer.dto.CustomerResponse;
import com.wave.ds.customer.entity.Brand;
import com.wave.ds.customer.entity.SiteType;
import com.wave.ds.customer.entity.Zone;
import com.wave.ds.customer.exception.CustomerNotFoundException;
import com.wave.ds.customer.service.CustomerService;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

// Security filters are switched off here: this class tests controller behaviour only.
// Token checks are covered by SecurityIntegrationTest.
@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerService service;

    private static final String VALID_JSON = """
            {
              "customerName": "Acme Traders",
              "contactNumber": "9876543210",
              "address": "12 Main Road",
              "city": "Delhi",
              "areaSector": "Sector 5",
              "zone": "NORTH_EAST_DELHI",
              "siteType": "SINGLE_SITE_POINT",
              "pincode": "110094",
              "customerTypeId": 1,
              "brand": "BISLERI",
              "companyId": 2,
              "startDate": "2026-11-01"
            }
            """;

    @Test
    void create_returns201WithLocation() throws Exception {
        CustomerResponse created = new CustomerResponse(7L, "Acme Traders", null, "9876543210", null,
                "12 Main Road", "Delhi", "Sector 5", Zone.NORTH_EAST_DELHI, "North East Delhi",
                SiteType.SINGLE_SITE_POINT, "Single-site point", "110094", 1L, "Commercial",
                Brand.BISLERI, "Bisleri", 2L, "Sample Company A", null, null,
                LocalDate.of(2026, 11, 1), null, null);
        when(service.create(any())).thenReturn(created);

        mockMvc.perform(post("/api/v1/customers").contentType(MediaType.APPLICATION_JSON).content(VALID_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/customers/7"))
                .andExpect(jsonPath("$.id").value(7));
    }

    @Test
    void create_returns400WithFieldErrorsWhenInvalid() throws Exception {
        String invalid = VALID_JSON
                .replace("\"9876543210\"", "\"123\"")
                .replace("\"110094\"", "\"12\"");

        mockMvc.perform(post("/api/v1/customers").contentType(MediaType.APPLICATION_JSON).content(invalid))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.contactNumber").value("Contact number must be 10 digits"))
                .andExpect(jsonPath("$.fieldErrors.pincode").value("Pincode must be 6 digits"));
    }

    @Test
    void create_returns400ForUnknownZone() throws Exception {
        mockMvc.perform(post("/api/v1/customers").contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_JSON.replace("NORTH_EAST_DELHI", "MARS")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void get_returns404WhenMissing() throws Exception {
        when(service.get(99L)).thenThrow(new CustomerNotFoundException(99L));

        mockMvc.perform(get("/api/v1/customers/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Customer not found with id 99"));
    }
}
