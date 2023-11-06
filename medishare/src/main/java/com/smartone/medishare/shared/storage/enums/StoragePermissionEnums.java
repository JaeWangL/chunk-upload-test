package com.smartone.medishare.shared.storage.enums;

import com.azure.storage.blob.sas.BlobSasPermission;
import com.smartone.medishare.exceptions.StoragePermissionInvalidException;

public enum StoragePermissionEnums {
    READ("r", new BlobSasPermission().setReadPermission(true)),
    WRITE("w", new BlobSasPermission().setWritePermission(true)),
    DELETE("d", new BlobSasPermission().setDeletePermission(true)),
    ADD("a", new BlobSasPermission().setAddPermission(true)),
    CREATE("c", new BlobSasPermission().setCreatePermission(true));

    private final String permissionString;
    private final BlobSasPermission blobSasPermission;

    StoragePermissionEnums(String permissionString, BlobSasPermission blobSasPermission) {
        this.permissionString = permissionString;
        this.blobSasPermission = blobSasPermission;
    }

    public static BlobSasPermission fromString(String permissionString) {
        for (StoragePermissionEnums permission : values()) {
            if (permission.permissionString.equals(permissionString)) {
                return permission.blobSasPermission;
            }
        }
        throw new StoragePermissionInvalidException(permissionString);
    }
}
