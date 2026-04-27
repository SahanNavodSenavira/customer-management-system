package com.example.customermanagement.service;

import com.example.customermanagement.dto.CustomerRequestDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BulkCustomerService {

    @Autowired
    private CustomerService customerService;
    
    private static final int BATCH_SIZE = 1000;
    
    // Remove @Transactional from here - we want each customer to be independent
    public String processBulkUpload(MultipartFile file) throws Exception {
        List<CustomerDTO> customers = parseExcelFile(file);
        
        int successCount = 0;
        int errorCount = 0;
        Map<String, List<String>> errorsByType = new HashMap<>();
        
        for (CustomerDTO customerDTO : customers) {
            try {
                CustomerRequestDTO request = new CustomerRequestDTO();
                request.setName(customerDTO.getName());
                request.setDateOfBirth(customerDTO.getDateOfBirth());
                request.setNicNumber(customerDTO.getNicNumber());
                request.setMobileNumbers(new ArrayList<>());
                request.setAddresses(new ArrayList<>());
                request.setFamilyMembers(new ArrayList<>());
                
                // This will create each customer in its own transaction
                customerService.createCustomer(request);
                successCount++;
                
            } catch (Exception e) {
                errorCount++;
                String errorMessage = e.getMessage();
                String customerInfo = customerDTO.getName() + " (NIC: " + customerDTO.getNicNumber() + ")";
                
                if (errorMessage.contains("NIC number already exists")) {
                    errorsByType.computeIfAbsent("Duplicate NIC", k -> new ArrayList<>()).add(customerInfo);
                } else if (errorMessage.contains("Date format") || errorMessage.contains("parse")) {
                    errorsByType.computeIfAbsent("Invalid Date Format", k -> new ArrayList<>()).add(customerInfo);
                } else if (errorMessage.contains("Name is required") || errorMessage.contains("empty")) {
                    errorsByType.computeIfAbsent("Missing Required Field", k -> new ArrayList<>()).add(customerInfo);
                } else {
                    errorsByType.computeIfAbsent("Other Errors", k -> new ArrayList<>()).add(customerInfo + " - " + errorMessage);
                }
            }
        }
        
        StringBuilder summary = new StringBuilder();
        summary.append("✅ Success: ").append(successCount)
               .append(", ❌ Failed: ").append(errorCount).append("\n\n");
        
        if (errorCount > 0) {
            summary.append("📊 Error Summary:\n");
            for (Map.Entry<String, List<String>> entry : errorsByType.entrySet()) {
                summary.append("\n• ").append(entry.getKey()).append(": ").append(entry.getValue().size()).append(" records\n");
                // Show first 10 errors only to avoid huge response
                int count = 0;
                for (String customer : entry.getValue()) {
                    if (count++ < 10) {
                        summary.append("  - ").append(customer).append("\n");
                    }
                }
                if (entry.getValue().size() > 10) {
                    summary.append("  - ... and ").append(entry.getValue().size() - 10).append(" more\n");
                }
            }
        } else {
            summary.append("🎉 All records processed successfully!");
        }
        
        return summary.toString();
    }
    
    private List<CustomerDTO> parseExcelFile(MultipartFile file) throws Exception {
        List<CustomerDTO> customers = new ArrayList<>();
        
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            
            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null) continue;
                
                CustomerDTO dto = new CustomerDTO();
                
                try {
                    // Column 0: Name
                    Cell nameCell = row.getCell(0);
                    if (nameCell == null || getCellValueAsString(nameCell).isEmpty()) {
                        throw new RuntimeException("Name is required");
                    }
                    dto.setName(getCellValueAsString(nameCell));
                    
                    // Column 1: Date of Birth
                    Cell dobCell = row.getCell(1);
                    if (dobCell == null) {
                        throw new RuntimeException("Date of Birth is required");
                    }
                    String dobStr = getCellValueAsString(dobCell);
                    dto.setDateOfBirth(LocalDate.parse(dobStr, dateFormatter));
                    
                    // Column 2: NIC Number
                    Cell nicCell = row.getCell(2);
                    if (nicCell == null || getCellValueAsString(nicCell).isEmpty()) {
                        throw new RuntimeException("NIC Number is required");
                    }
                    dto.setNicNumber(getCellValueAsString(nicCell));
                    
                    customers.add(dto);
                    
                } catch (Exception e) {
                    // If row has error, add to customers with error info
                    dto.setName("ERROR: " + e.getMessage());
                    customers.add(dto);
                }
            }
        }
        
        return customers;
    }
    
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate().toString();
                }
                return String.valueOf((long) cell.getNumericCellValue());
            default:
                return "";
        }
    }
    
    private static class CustomerDTO {
        private String name;
        private LocalDate dateOfBirth;
        private String nicNumber;
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public LocalDate getDateOfBirth() { return dateOfBirth; }
        public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
        public String getNicNumber() { return nicNumber; }
        public void setNicNumber(String nicNumber) { this.nicNumber = nicNumber; }
    }
}