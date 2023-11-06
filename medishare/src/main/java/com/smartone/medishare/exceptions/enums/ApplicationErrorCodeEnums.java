package com.smartone.medishare.exceptions.enums;

import org.springframework.http.HttpStatus;

public enum ApplicationErrorCodeEnums {
    INVALID_BLOB_PERMISSION(HttpStatus.BAD_REQUEST, 1001, "This is a specific error message for error code 1001."),
    GENERIC_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 9999, "A generic error occurred.");

    private final HttpStatus httpStatus;
    private final int errorCode;
    private final String errorMessage;

    ApplicationErrorCodeEnums(HttpStatus httpStatus, int errorCode, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
