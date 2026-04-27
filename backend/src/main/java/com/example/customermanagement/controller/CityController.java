package com.example.customermanagement.controller;

import com.example.customermanagement.dto.ApiResponseDTO;
import com.example.customermanagement.dto.CityDTO;
import com.example.customermanagement.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/cities")
@CrossOrigin(origins = "http://localhost:3000")
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<CityDTO>>>
            getAllCities() {

        List<CityDTO> cities = cityService.getAllCities();
        return ResponseEntity.ok(
            ApiResponseDTO.success(
                "Cities retrieved successfully", cities));
    }
}