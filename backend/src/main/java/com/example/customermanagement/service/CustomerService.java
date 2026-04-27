package com.example.customermanagement.service;

import com.example.customermanagement.dto.AddressDTO;
import com.example.customermanagement.dto.CustomerRequestDTO;
import com.example.customermanagement.dto.CustomerResponseDTO;
import com.example.customermanagement.dto.CustomerSummaryDTO;
import com.example.customermanagement.dto.FamilyMemberDTO;
import com.example.customermanagement.dto.MobileNumberDTO;
import com.example.customermanagement.entity.City;
import com.example.customermanagement.entity.Customer;
import com.example.customermanagement.entity.CustomerAddress;
import com.example.customermanagement.entity.CustomerFamily;
import com.example.customermanagement.entity.CustomerMobile;
import com.example.customermanagement.repository.CityRepository;
import com.example.customermanagement.repository.CustomerFamilyRepository;
import com.example.customermanagement.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CustomerFamilyRepository customerFamilyRepository;

    // =====================
    // CREATE CUSTOMER
    // =====================
    public CustomerResponseDTO createCustomer(
            CustomerRequestDTO request) {

        // Step 1 - Check if NIC already exists
        if (customerRepository.existsByNicNumber(
                request.getNicNumber())) {
            throw new RuntimeException(
                "NIC number already exists: "
                + request.getNicNumber());
        }

        // Step 2 - Create customer entity
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setNicNumber(request.getNicNumber());

        // Step 3 - Add mobile numbers
        if (request.getMobileNumbers() != null
                && !request.getMobileNumbers().isEmpty()) {
            for (MobileNumberDTO mobileDTO 
                    : request.getMobileNumbers()) {
                CustomerMobile mobile = new CustomerMobile();
                mobile.setMobileNumber(
                    mobileDTO.getMobileNumber());
                mobile.setIsPrimary(
                    mobileDTO.getIsPrimary() != null 
                    ? mobileDTO.getIsPrimary() : false);
                mobile.setCustomer(customer);
                customer.getMobileNumbers().add(mobile);
            }
        }

        // Step 4 - Add addresses
        if (request.getAddresses() != null
                && !request.getAddresses().isEmpty()) {
            for (AddressDTO addressDTO 
                    : request.getAddresses()) {
                City city = cityRepository
                    .findById(addressDTO.getCityId())
                    .orElseThrow(() -> new RuntimeException(
                        "City not found: "
                        + addressDTO.getCityId()));

                CustomerAddress address = 
                    new CustomerAddress();
                address.setAddressLine1(
                    addressDTO.getAddressLine1());
                address.setAddressLine2(
                    addressDTO.getAddressLine2());
                address.setCity(city);
                address.setIsPrimary(
                    addressDTO.getIsPrimary() != null 
                    ? addressDTO.getIsPrimary() : false);
                address.setCustomer(customer);
                customer.getAddresses().add(address);
            }
        }

        // Step 5 - Save customer
        // Mobiles and addresses saved automatically
        // due to CascadeType.ALL
        Customer savedCustomer = 
            customerRepository.save(customer);
        customerRepository.flush();

        // Step 6 - Add family members
        // Done after save so customer has an ID
        if (request.getFamilyMembers() != null
                && !request.getFamilyMembers().isEmpty()) {
            for (FamilyMemberDTO familyDTO 
                    : request.getFamilyMembers()) {
                linkFamilyMember(
                    savedCustomer,
                    familyDTO.getFamilyMemberId(),
                    familyDTO.getRelationshipType());
            }
        }

        // Step 7 - Return response
        return getCustomerById(savedCustomer.getId());
    }

    // =====================
    // UPDATE CUSTOMER
    // =====================
    public CustomerResponseDTO updateCustomer(
            Long id, CustomerRequestDTO request) {

        // Step 1 - Find existing customer
        Customer customer = customerRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException(
                "Customer not found: " + id));

        // Step 2 - Check NIC if it changed
        if (!customer.getNicNumber()
                .equals(request.getNicNumber())) {
            if (customerRepository.existsByNicNumber(
                    request.getNicNumber())) {
                throw new RuntimeException(
                    "NIC number already exists: "
                    + request.getNicNumber());
            }
        }

        // Step 3 - Update basic fields
        customer.setName(request.getName());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setNicNumber(request.getNicNumber());

        // Step 4 - Update mobile numbers
        // Clear existing and add new ones
        customer.getMobileNumbers().clear();
        if (request.getMobileNumbers() != null
                && !request.getMobileNumbers().isEmpty()) {
            for (MobileNumberDTO mobileDTO 
                    : request.getMobileNumbers()) {
                CustomerMobile mobile = new CustomerMobile();
                mobile.setMobileNumber(
                    mobileDTO.getMobileNumber());
                mobile.setIsPrimary(
                    mobileDTO.getIsPrimary() != null 
                    ? mobileDTO.getIsPrimary() : false);
                mobile.setCustomer(customer);
                customer.getMobileNumbers().add(mobile);
            }
        }

        // Step 5 - Update addresses
        // Clear existing and add new ones
        customer.getAddresses().clear();
        if (request.getAddresses() != null
                && !request.getAddresses().isEmpty()) {
            for (AddressDTO addressDTO 
                    : request.getAddresses()) {
                City city = cityRepository
                    .findById(addressDTO.getCityId())
                    .orElseThrow(() -> new RuntimeException(
                        "City not found: "
                        + addressDTO.getCityId()));

                CustomerAddress address = 
                    new CustomerAddress();
                address.setAddressLine1(
                    addressDTO.getAddressLine1());
                address.setAddressLine2(
                    addressDTO.getAddressLine2());
                address.setCity(city);
                address.setIsPrimary(
                    addressDTO.getIsPrimary() != null 
                    ? addressDTO.getIsPrimary() : false);
                address.setCustomer(customer);
                customer.getAddresses().add(address);
            }
        }

        // Step 6 - Save updated customer
        customerRepository.save(customer);
        customerRepository.flush();

        // Step 7 - Return updated response
        return getCustomerById(id);
    }

    // =====================
    // GET SINGLE CUSTOMER
    // =====================
    public CustomerResponseDTO getCustomerById(Long id) {

        // Simple findById
        // @Transactional keeps session open
        // so lazy loading works correctly
        Customer customer = customerRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException(
                "Customer not found: " + id));

        return mapToResponseDTO(customer);
    }

    // =====================
    // GET ALL CUSTOMERS
    // Paginated for table view
    // =====================
    public Page<CustomerSummaryDTO> getAllCustomers(
            int page, int size) {

        Pageable pageable = PageRequest.of(
            page, size, Sort.by("name").ascending());

        Page<Customer> customers = 
            customerRepository.findAll(pageable);

        // Map to summary DTO
        // Only basic fields - no heavy relations loaded
        return customers.map(customer -> {
            CustomerSummaryDTO summary = 
                new CustomerSummaryDTO();
            summary.setId(customer.getId());
            summary.setName(customer.getName());
            summary.setDateOfBirth(
                customer.getDateOfBirth());
            summary.setNicNumber(
                customer.getNicNumber());
            return summary;
        });
    }

    // =====================
    // LINK FAMILY MEMBER
    // =====================
    public void linkFamilyMember(
            Customer customer,
            Long familyMemberId,
            String relationshipType) {

        // Check family member exists
        Customer familyMember = customerRepository
            .findById(familyMemberId)
            .orElseThrow(() -> new RuntimeException(
                "Family member not found: "
                + familyMemberId));

        // Check not linking to self
        if (customer.getId().equals(familyMemberId)) {
            throw new RuntimeException(
                "Customer cannot be their own "
                + "family member");
        }

        // Check link does not already exist
        if (customerFamilyRepository
                .existsByCustomerIdAndFamilyMemberId(
                    customer.getId(), familyMemberId)) {
            return; // Already linked skip
        }

        // Direction 1: Customer → Family Member
        CustomerFamily link1 = new CustomerFamily();
        link1.setCustomer(customer);
        link1.setFamilyMember(familyMember);
        link1.setRelationshipType(relationshipType);
        customerFamilyRepository.save(link1);

        // Direction 2: Family Member → Customer
        CustomerFamily link2 = new CustomerFamily();
        link2.setCustomer(familyMember);
        link2.setFamilyMember(customer);
        link2.setRelationshipType(relationshipType);
        customerFamilyRepository.save(link2);
    }

    // =====================
    // HELPER - Map Entity to DTO
    // =====================
    private CustomerResponseDTO mapToResponseDTO(
            Customer customer) {

        CustomerResponseDTO response = 
            new CustomerResponseDTO();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setDateOfBirth(customer.getDateOfBirth());
        response.setNicNumber(customer.getNicNumber());
        response.setCreatedAt(customer.getCreatedAt());
        response.setUpdatedAt(customer.getUpdatedAt());

        // Map mobile numbers
        List<MobileNumberDTO> mobiles = new ArrayList<>();
        if (customer.getMobileNumbers() != null) {
            mobiles = customer.getMobileNumbers()
                .stream().map(mobile -> {
                    MobileNumberDTO dto = 
                        new MobileNumberDTO();
                    dto.setId(mobile.getId());
                    dto.setMobileNumber(
                        mobile.getMobileNumber());
                    dto.setIsPrimary(mobile.getIsPrimary());
                    return dto;
                }).collect(Collectors.toList());
        }
        response.setMobileNumbers(mobiles);

        // Map addresses
        List<AddressDTO> addresses = new ArrayList<>();
        if (customer.getAddresses() != null) {
            addresses = customer.getAddresses()
                .stream().map(addr -> {
                    AddressDTO dto = new AddressDTO();
                    dto.setId(addr.getId());
                    dto.setAddressLine1(
                        addr.getAddressLine1());
                    dto.setAddressLine2(
                        addr.getAddressLine2());
                    dto.setCityId(addr.getCity().getId());
                    dto.setCityName(
                        addr.getCity().getName());
                    dto.setCountryName(addr.getCity()
                        .getCountry().getName());
                    dto.setIsPrimary(addr.getIsPrimary());
                    return dto;
                }).collect(Collectors.toList());
        }
        response.setAddresses(addresses);

        // Map family members
        List<FamilyMemberDTO> familyMembers = 
            new ArrayList<>();
        if (customer.getFamilyMembers() != null) {
            familyMembers = customer.getFamilyMembers()
                .stream().map(family -> {
                    FamilyMemberDTO dto = 
                        new FamilyMemberDTO();
                    dto.setFamilyMemberId(
                        family.getFamilyMember().getId());
                    dto.setFamilyMemberName(
                        family.getFamilyMember().getName());
                    dto.setFamilyMemberNic(
                        family.getFamilyMember()
                        .getNicNumber());
                    dto.setRelationshipType(
                        family.getRelationshipType());
                    return dto;
                }).collect(Collectors.toList());
        }
        response.setFamilyMembers(familyMembers);

        return response;
    }
}