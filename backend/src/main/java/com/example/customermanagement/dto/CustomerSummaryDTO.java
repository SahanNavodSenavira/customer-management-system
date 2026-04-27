package com.example.customermanagement.dto;

import lombok.Data;
import java.time.LocalDate;

//This is used for the TABLE VIEW
//Only shows basic info - no heavy relations loaded
//This keeps the list page fast and efficient

@Data
public class CustomerSummaryDTO {
	private Long id;
    private String name;
    private LocalDate dateOfBirth;
    private String nicNumber;

}
