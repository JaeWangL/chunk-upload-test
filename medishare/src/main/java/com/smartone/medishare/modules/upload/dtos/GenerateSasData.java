package com.smartone.medishare.modules.upload.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record GenerateSasData(
    @NotBlank(message = "{NotBlank.blobName}")
    @Schema(description = "blobName", example = "file.mp4")
    String blobName,

    @NotBlank(message = "{NotBlank.permission}")
    @Schema(description = "permission", example = "r")
    String permission
) {
}
