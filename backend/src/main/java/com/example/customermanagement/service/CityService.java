package com.example.customermanagement.service;

import com.example.customermanagement.dto.CityDTO;
import com.example.customermanagement.entity.City;
import com.example.customermanagement.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    // Get all cities with country info
    // Used for dropdown in frontend
    public List<CityDTO> getAllCities() {
        List<City> cities = 
            cityRepository.findAllWithCountry();

        return cities.stream().map(city -> {
            CityDTO dto = new CityDTO();
            dto.setCityId(city.getId());
            dto.setCityName(city.getName());
            dto.setCountryName(
                city.getCountry().getName());
            return dto;
        }).collect(Collectors.toList());
    }
}