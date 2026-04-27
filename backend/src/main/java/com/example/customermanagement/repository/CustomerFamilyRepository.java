package com.example.customermanagement.repository;

import com.example.customermanagement.entity.CustomerFamily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CustomerFamilyRepository extends JpaRepository<CustomerFamily, Long> {

    // Check if family link already exists
    boolean existsByCustomerIdAndFamilyMemberId(
        Long customerId, 
        Long familyMemberId
    );

    // Find all family members of a customer
    List<CustomerFamily> findByCustomerId(Long customerId);

    // Delete family link in both directions
    // When A removes B as family, also remove B's link to A
    @Query("DELETE FROM CustomerFamily f WHERE " +
           "(f.customer.id = :customerId AND f.familyMember.id = :memberId) OR " +
           "(f.customer.id = :memberId AND f.familyMember.id = :customerId)")
    @org.springframework.data.jpa.repository.Modifying
    void deleteFamilyLinkBothDirections(
        @Param("customerId") Long customerId, 
        @Param("memberId") Long memberId
    );
}