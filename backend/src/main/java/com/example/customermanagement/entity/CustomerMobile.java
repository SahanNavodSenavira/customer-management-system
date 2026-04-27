package com.example.customermanagement.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "customer_mobile")

public class CustomerMobile {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    // Many mobiles belong to one customer
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "customer_id", nullable = false)
	    private Customer customer;

	    @Column(name = "mobile_number", nullable = false, length = 20)
	    private String mobileNumber;

	    @Column(name = "is_primary")
	    private Boolean isPrimary = false;

	    @Column(name = "created_at")
	    private LocalDateTime createdAt;

	    @PrePersist
	    protected void onCreate() {
	        createdAt = LocalDateTime.now();
	    }

}
