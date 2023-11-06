package com.smartone.medishare.exceptions.dtos;

public record ErrorResponseDTO(
    int statusCode,
    int errorCode,
    String errorMessage
) {}