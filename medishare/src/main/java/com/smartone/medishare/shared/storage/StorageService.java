package com.smartone.medishare.shared.storage;

import java.util.List;

public interface StorageService {
    String generateSasUrl(String containerName, String blobName, String permission);

    void deleteBlob(String containerName, String blobName);

    void deleteBlobs(String containerName, List<String> blobNames);
}
