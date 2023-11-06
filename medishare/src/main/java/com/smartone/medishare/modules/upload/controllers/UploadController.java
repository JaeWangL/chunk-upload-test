package com.smartone.medishare.modules.upload.controllers;

import com.smartone.medishare.modules.upload.dtos.GenerateSasData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartone.medishare.shared.storage.StorageService;

@Tag(name = "Upload API", description = "파일서버의 SAS URL을 생성하고 파일을 관리하고 위한 API")
@RestController
@RequestMapping("/upload")
public class UploadController {
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    private final StorageService storageSvc;

    public UploadController(StorageService storageSvc) {
        this.storageSvc = storageSvc;
    }

    @Operation(
        summary = "Video를 위한 SAS URL 생성",
        description = "videos 컨테이너에 비디오를 업로드 및 다운로드 하기위한 SAS URL 생성")
    @PostMapping("/sas/videos")
    public ResponseEntity<String> generateSasUrl(@RequestBody @Valid GenerateSasData sasRequest) {
        logger.info("Generating SAS URL for blob: {}, with permission: {}", sasRequest.blobName(), sasRequest.permission());
        String sasUrl = storageSvc.generateSasUrl("videos", sasRequest.blobName(), sasRequest.permission());
        return ResponseEntity.ok(sasUrl);
    }
}
