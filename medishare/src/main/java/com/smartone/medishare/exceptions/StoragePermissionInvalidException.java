package com.smartone.medishare.exceptions;

import com.smartone.medishare.exceptions.enums.ApplicationErrorCodeEnums;

public class StoragePermissionInvalidException extends IllegalArgumentException {
    private final ApplicationErrorCodeEnums errorCode;

    public StoragePermissionInvalidException(String permissionString) {
        super("Invalid storage permission: " + permissionString);
        this.errorCode = ApplicationErrorCodeEnums.INVALID_BLOB_PERMISSION;
    }

    public ApplicationErrorCodeEnums getErrorCode() {
        return errorCode;
    }
}