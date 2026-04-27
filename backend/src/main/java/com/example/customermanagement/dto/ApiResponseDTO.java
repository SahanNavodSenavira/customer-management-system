package com.example.customermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// Standard response wrapper for ALL API responses
// Every response from the API looks the same
@Data
@AllArgsConstructor
public class ApiResponseDTO<T> {

    private boolean success;
    private String message;
    private T data;

    // Success response
    public static <T> ApiResponseDTO<T> success(String message, T data) {
        return new ApiResponseDTO<>(true, message, data);
    }

    // Error response
    public static <T> ApiResponseDTO<T> error(String message) {
        return new ApiResponseDTO<>(false, message, null);
    }
}