package com.example.customermanagement.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
public class CustomerResponseDTO {
	private Long id;
    private String name;
    private LocalDate dateOfBirth;
    private String nicNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // All related data
    private List<MobileNumberDTO> mobileNumbers = new ArrayList<>();
    private List<AddressDTO> addresses = new ArrayList<>();
    private List<FamilyMemberDTO> familyMembers = new ArrayList<>();

}
