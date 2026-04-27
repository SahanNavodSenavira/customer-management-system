package com.example.customermanagement.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddressDTO {
	private Long id;

    @NotBlank(message = "Address line 1 is required")
    private String addressLine1;

    private String addressLine2;

    @NotNull(message = "City is required")
    private Long cityId;

    // These come back in response
    private String cityName;
    private String countryName;

    private Boolean isPrimary = false;

}
