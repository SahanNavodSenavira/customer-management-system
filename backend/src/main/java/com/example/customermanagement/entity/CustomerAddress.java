package com.example.customermanagement.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "customer_address")
public class CustomerAddress {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    // Many addresses belong to one customer
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "customer_id", nullable = false)
	    private Customer customer;

	    @Column(name = "address_line1", nullable = false)
	    private String addressLine1;

	    @Column(name = "address_line2")
	    private String addressLine2;

	    // Many addresses can be in one city
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "city_id", nullable = false)
	    private City city;

	    @Column(name = "is_primary")
	    private Boolean isPrimary = false;

	    @Column(name = "created_at")
	    private LocalDateTime createdAt;

	    @PrePersist
	    protected void onCreate() {
	        createdAt = LocalDateTime.now();
	    }

}
