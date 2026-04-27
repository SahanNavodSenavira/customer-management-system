package com.example.customermanagement.repository;

import com.example.customermanagement.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    // Get all cities with their country in ONE query
    // This avoids multiple DB calls
    @Query("SELECT c FROM City c JOIN FETCH c.country")
    List<City> findAllWithCountry();

    // Find all cities by country id
    List<City> findByCountryId(Long countryId);
}
