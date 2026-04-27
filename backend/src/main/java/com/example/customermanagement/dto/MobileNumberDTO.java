package com.example.customermanagement.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class MobileNumberDTO {
	private Long id;

    @NotBlank(message = "Mobile number is required")
    private String mobileNumber;

    private Boolean isPrimary = false;

}
