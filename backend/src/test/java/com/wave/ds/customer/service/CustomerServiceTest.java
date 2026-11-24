package com.wave.ds.customer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wave.ds.common.exception.BadRequestException;
import com.wave.ds.customer.dto.CustomerRequest;
import com.wave.ds.customer.dto.CustomerResponse;
import com.wave.ds.customer.entity.Brand;
import com.wave.ds.customer.entity.Customer;
import com.wave.ds.customer.entity.SiteType;
import com.wave.ds.customer.entity.Zone;
import com.wave.ds.customer.exception.CustomerNotFoundException;
import com.wave.ds.customer.mapper.CustomerMapper;
import com.wave.ds.customer.repository.CustomerRepository;
import com.wave.ds.lookup.entity.LookupCategory;
import com.wave.ds.lookup.entity.LookupValue;
import com.wave.ds.lookup.service.LookupService;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    CustomerRepository repository;
    @Mock
    LookupService lookupService;

    CustomerServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CustomerServiceImpl(repository, lookupService, new CustomerMapper());
    }

    private CustomerRequest request() {
        return new CustomerRequest("Acme Traders", "  ", "9876543210", "", "12 Main Road",
                "Delhi", "Sector 5", Zone.NORTH_EAST_DELHI, SiteType.MULTI_SITE_POINT, "110094",
                1L, Brand.WAVE, 2L, null, "  ", LocalDate.of(2026, 11, 1));
    }

    @Test
    void create_savesCustomerAndNormalizesBlankOptionalFields() {
        when(lookupService.requireActive(1L, LookupCategory.CUSTOMER_TYPE))
                .thenReturn(new LookupValue(LookupCategory.CUSTOMER_TYPE, "Commercial", 1));
        when(lookupService.requireActive(2L, LookupCategory.COMPANY))
                .thenReturn(new LookupValue(LookupCategory.COMPANY, "Sample Company A", 1));
        when(repository.save(any(Customer.class))).thenAnswer(inv -> inv.getArgument(0));

        CustomerResponse response = service.create(request());

        assertThat(response.customerName()).isEqualTo("Acme Traders");
        assertThat(response.email()).isNull();
        assertThat(response.alternateNumber()).isNull();
        assertThat(response.oldCustomerId()).isNull();
        assertThat(response.zoneLabel()).isEqualTo("North East Delhi");
        assertThat(response.customerType()).isEqualTo("Commercial");
    }

    @Test
    void create_rejectsUnknownCustomerType() {
        when(lookupService.requireActive(1L, LookupCategory.CUSTOMER_TYPE))
                .thenThrow(new BadRequestException("Invalid CUSTOMER_TYPE id: 1"));

        assertThatThrownBy(() -> service.create(request())).isInstanceOf(BadRequestException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void get_throwsWhenMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(99L)).isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void delete_throwsWhenMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(99L)).isInstanceOf(CustomerNotFoundException.class);
        verify(repository, never()).delete(any(Customer.class));
    }
}
