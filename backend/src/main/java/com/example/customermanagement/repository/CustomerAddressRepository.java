package com.example.customermanagement.repository;

import com.example.customermanagement.entity.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long> {

    // Set all addresses to not primary for a customer
    // Used before setting a new primary address
    @Modifying
    @Query("UPDATE CustomerAddress a SET a.isPrimary = false WHERE a.customer.id = :customerId")
    void resetPrimaryForCustomer(@Param("customerId") Long customerId);
}