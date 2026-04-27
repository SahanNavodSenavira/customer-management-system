package com.example.customermanagement.service;

import com.example.customermanagement.dto.CustomerRequestDTO;
import com.example.customermanagement.dto.CustomerResponseDTO;
import com.example.customermanagement.entity.Customer;
import com.example.customermanagement.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private CustomerRequestDTO testRequest;
    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        testRequest = new CustomerRequestDTO();
        testRequest.setName("Test Customer");
        testRequest.setDateOfBirth(LocalDate.of(1990, 1, 15));
        testRequest.setNicNumber("TEST123V");
        
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setName("Test Customer");
        testCustomer.setNicNumber("TEST123V");
    }

    @Test
    void testCreateCustomer_Success() {
        when(customerRepository.existsByNicNumber(anyString())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(testCustomer));

        CustomerResponseDTO result = customerService.createCustomer(testRequest);

        assertNotNull(result);
        assertEquals("Test Customer", result.getName());
        System.out.println("✅ Test passed: Customer created successfully");
    }

    @Test
    void testCreateCustomer_DuplicateNIC_ThrowsException() {
        when(customerRepository.existsByNicNumber("TEST123V")).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            customerService.createCustomer(testRequest);
        });

        assertTrue(exception.getMessage().contains("NIC number already exists"));
        System.out.println("✅ Test passed: Duplicate NIC detected");
    }

    @Test
    void testGetCustomerById_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));

        CustomerResponseDTO result = customerService.getCustomerById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        System.out.println("✅ Test passed: Customer found by ID");
    }

    @Test
    void testGetCustomerById_NotFound_ThrowsException() {
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            customerService.getCustomerById(999L);
        });

        assertTrue(exception.getMessage().contains("Customer not found"));
        System.out.println("✅ Test passed: Customer not found handled correctly");
    }
}