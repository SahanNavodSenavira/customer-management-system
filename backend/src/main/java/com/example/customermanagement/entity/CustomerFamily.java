package com.example.customermanagement.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "customer_family")

public class CustomerFamily {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The customer who owns this family link
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // The family member (also a customer)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_member_id", nullable = false)
    private Customer familyMember;

    @Column(name = "relationship_type", nullable = false, length = 50)
    private String relationshipType; // "Spouse", "Parent", "Child", "Sibling"

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}
