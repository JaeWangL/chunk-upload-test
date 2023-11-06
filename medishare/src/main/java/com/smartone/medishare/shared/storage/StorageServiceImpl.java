package com.smartone.medishare.shared.storage;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.azure.storage.common.StorageSharedKeyCredential;
import com.azure.storage.common.sas.SasProtocol;
import com.smartone.medishare.configs.AzureConfigs;
import com.smartone.medishare.shared.storage.enums.StoragePermissionEnums;
import com.smartone.medishare.shared.helpers.FileHelpers;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class StorageServiceImpl implements StorageService {
    private final AzureConfigs config;
    private final BlobServiceClient blobServiceClient;

    public StorageServiceImpl(AzureConfigs config) {
        this.config = config;
        this.blobServiceClient = new BlobServiceClientBuilder()
            .endpoint(String.format("https://%s.blob.core.windows.net", config.getAccountName()))
            .credential(new StorageSharedKeyCredential(config.getAccountName(), config.getAccountKey()))
            .buildClient();
    }

    @Override
    public String generateSasUrl(String containerName, String blobName, String permission) {
        var blobSasPermission = StoragePermissionEnums.fromString(permission);
        var expiryTime = OffsetDateTime.now(ZoneOffset.UTC).plus(
            Duration.parse(config.getSasDuration()));
        var containerClient = blobServiceClient.getBlobContainerClient(containerName);
        var blobClient = containerClient.getBlobClient(FileHelpers.appendTimestamp(blobName));
        String sasToken = blobClient.generateSas(new BlobServiceSasSignatureValues(expiryTime, blobSasPermission)
            .setProtocol(SasProtocol.HTTPS_HTTP));

        return String.format("%s?%s", blobClient.getBlobUrl(), sasToken);
    }

    @Override
    public void deleteBlob(String containerName, String blobName) {
        var blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);
        var blobClient = blobContainerClient.getBlobClient(blobName);
        blobClient.delete();
    }

    @Override
    public void deleteBlobs(String containerName, List<String> blobNames) {
        var blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);

        for (String blobName : blobNames) {
            var blobClient = blobContainerClient.getBlobClient(blobName);
            blobClient.delete();
        }
    }
}
