package com.smartone.medishare.exceptions.handlers;

import com.smartone.medishare.exceptions.StoragePermissionInvalidException;
import com.smartone.medishare.exceptions.dtos.ErrorResponseDTO;
import com.smartone.medishare.exceptions.enums.ApplicationErrorCodeEnums;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(HttpServletRequest request, Exception e) {
        return handleException(request, e, ApplicationErrorCodeEnums.GENERIC_ERROR);
    }

    @ExceptionHandler(StoragePermissionInvalidException.class)
    public ResponseEntity<ErrorResponseDTO> handleStoragePermissionNotFound(HttpServletRequest request, StoragePermissionInvalidException e) {
        return handleException(request, e, ApplicationErrorCodeEnums.INVALID_BLOB_PERMISSION);
    }

    private ResponseEntity<ErrorResponseDTO> handleException(HttpServletRequest request, Exception e, ApplicationErrorCodeEnums errorCode) {
        try {
            var status = errorCode.getHttpStatus();
            var correlationId = (String) request.getAttribute("correlationId");
            var startTime = (Long) request.getAttribute("startTime");
            long timeTaken = System.currentTimeMillis() - startTime;

            MDC.put("correlationId", correlationId);
            MDC.put("timeTaken", Long.toString(timeTaken));
            MDC.put("status", Integer.toString(status.value()));
            MDC.put("errorCode", Integer.toString(errorCode.getErrorCode()));
            MDC.put("errorMessage", errorCode.getErrorMessage());
            MDC.put("exception", e.getClass().getSimpleName());
            MDC.put("exceptionMessage", e.getMessage());
            MDC.put("uri", request.getRequestURI());
            MDC.put("userAgent", request.getHeader("User-Agent"));
            MDC.put("acceptLanguage", request.getHeader("Accept-Language"));

            if (status.is4xxClientError()) {
                logger.warn("Client error: ", e);
            } else if (status.is5xxServerError()) {
                logger.error("Server error: ", e);
            } else {
                logger.error("Unknown Error: ", e);
            }

            ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                ApplicationErrorCodeEnums.GENERIC_ERROR.getHttpStatus().value(),
                ApplicationErrorCodeEnums.GENERIC_ERROR.getErrorCode(),
                ApplicationErrorCodeEnums.GENERIC_ERROR.getErrorMessage()
            );

            return new ResponseEntity<>(errorResponse, ApplicationErrorCodeEnums.GENERIC_ERROR.getHttpStatus());
        } finally {
            MDC.clear();
        }
    }
}
