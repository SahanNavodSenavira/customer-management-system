package com.example.customermanagement.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;

@Data
public class FamilyMemberDTO {
	// ID of the existing customer who is the family member
    @NotNull(message = "Family member ID is required")
    private Long familyMemberId;

    // Name comes back in response
    private String familyMemberName;

    // NIC comes back in response
    private String familyMemberNic;

    @NotBlank(message = "Relationship type is required")
    private String relationshipType;

}
