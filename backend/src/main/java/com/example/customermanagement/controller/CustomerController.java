package com.example.customermanagement.controller;

import com.example.customermanagement.dto.ApiResponseDTO;
import com.example.customermanagement.dto.CustomerRequestDTO;
import com.example.customermanagement.dto.CustomerResponseDTO;
import com.example.customermanagement.dto.CustomerSummaryDTO;
import com.example.customermanagement.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import org.springframework.web.multipart.MultipartFile;
import com.example.customermanagement.service.BulkCustomerService;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "http://localhost:3000")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // =====================
    // POST /api/customers
    // Create a new customer
    // =====================
    @PostMapping
    public ResponseEntity<ApiResponseDTO<CustomerResponseDTO>>
            createCustomer(
                @Valid @RequestBody 
                CustomerRequestDTO request) {

        CustomerResponseDTO response = 
            customerService.createCustomer(request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponseDTO.success(
                "Customer created successfully", response));
    }

    // =====================
    // PUT /api/customers/{id}
    // Update existing customer
    // =====================
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<CustomerResponseDTO>>
            updateCustomer(
                @PathVariable Long id,
                @Valid @RequestBody 
                CustomerRequestDTO request) {

        CustomerResponseDTO response = 
            customerService.updateCustomer(id, request);

        return ResponseEntity.ok(
            ApiResponseDTO.success(
                "Customer updated successfully", response));
    }

    // =====================
    // GET /api/customers/{id}
    // Get single customer with all details
    // =====================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<CustomerResponseDTO>>
            getCustomerById(@PathVariable Long id) {

        CustomerResponseDTO response = 
            customerService.getCustomerById(id);

        return ResponseEntity.ok(
            ApiResponseDTO.success(
                "Customer retrieved successfully", response));
    }

    // =====================
    // GET /api/customers
    // Get all customers (paginated)
    // Example: /api/customers?page=0&size=20
    // =====================
    @GetMapping
    public ResponseEntity<ApiResponseDTO<Page<CustomerSummaryDTO>>>
            getAllCustomers(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "20") int size) {

        Page<CustomerSummaryDTO> response = 
            customerService.getAllCustomers(page, size);

        return ResponseEntity.ok(
            ApiResponseDTO.success(
                "Customers retrieved successfully", response));
    }
 // =====================
 // POST /api/customers/bulk-upload
 // Bulk upload customers from Excel/CSV file
 // Supports up to 1,000,000 records (processed in batches)
 // =====================
    @Autowired
    private BulkCustomerService bulkCustomerService;

    @PostMapping("/bulk-upload")
    public ResponseEntity<ApiResponseDTO<String>> bulkUploadCustomers(
            @RequestParam("file") MultipartFile file) {
        
        try {
            String result = bulkCustomerService.processBulkUpload(file);
            return ResponseEntity.ok(
                ApiResponseDTO.success("Bulk upload completed: " + result, null));
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDTO.error("Bulk upload failed: " + e.getMessage()));
        }
    }
    
    
}