package com.example.customermanagement.controller;

import com.example.customermanagement.dto.CustomerRequestDTO;
import com.example.customermanagement.dto.CustomerResponseDTO;
import com.example.customermanagement.service.CustomerService;
import com.example.customermanagement.service.BulkCustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @MockBean  // Add this - it was missing!
    private BulkCustomerService bulkCustomerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateCustomer() throws Exception {
        CustomerRequestDTO request = new CustomerRequestDTO();
        request.setName("API Test Customer");
        request.setDateOfBirth(LocalDate.of(1990, 1, 15));
        request.setNicNumber("APITEST123V");

        CustomerResponseDTO response = new CustomerResponseDTO();
        response.setId(1L);
        response.setName("API Test Customer");
        response.setNicNumber("APITEST123V");

        when(customerService.createCustomer(any())).thenReturn(response);

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("API Test Customer"));
        
        System.out.println("✅ Test passed: Create customer API works");
    }

    @Test
    void testGetCustomerById() throws Exception {
        CustomerResponseDTO response = new CustomerResponseDTO();
        response.setId(1L);
        response.setName("Test Customer");

        when(customerService.getCustomerById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Test Customer"));
        
        System.out.println("✅ Test passed: Get customer API works");
    }

    @Test
    void testGetAllCustomers() throws Exception {
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk());
        
        System.out.println("✅ Test passed: Get all customers API works");
    }
}