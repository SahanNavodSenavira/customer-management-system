package com.example.customermanagement.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



@Data
public class CustomerRequestDTO {
	// Mandatory fields
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotBlank(message = "NIC number is required")
    private String nicNumber;

    // Optional fields
    private List<MobileNumberDTO> mobileNumbers = new ArrayList<>();

    private List<AddressDTO> addresses = new ArrayList<>();

    private List<FamilyMemberDTO> familyMembers = new ArrayList<>();

}
