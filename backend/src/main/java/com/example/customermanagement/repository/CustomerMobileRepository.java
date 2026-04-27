package com.example.customermanagement.repository;

import com.example.customermanagement.entity.CustomerMobile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerMobileRepository extends JpaRepository<CustomerMobile, Long> {

    // Check if mobile number already exists for this customer
    boolean existsByCustomerIdAndMobileNumber(Long customerId, String mobileNumber);

    // Set all mobiles to not primary for a customer
    // Used before setting a new primary number
    @Modifying
    @Query("UPDATE CustomerMobile m SET m.isPrimary = false WHERE m.customer.id = :customerId")
    void resetPrimaryForCustomer(@Param("customerId") Long customerId);
}
