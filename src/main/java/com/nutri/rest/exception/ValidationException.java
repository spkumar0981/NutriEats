package com.nutri.rest.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.nutri.rest.exception.ErrorCode.M00G0001;


public class ValidationException extends RuntimeException {

    private final UUID errorId;
    private final ErrorCode errorCode;
    private final List<ErrorInfo> errors = new ArrayList<>();

    public ValidationException(String message) {
        this(message, UUID.randomUUID(), M00G0001);
    }

    public ValidationException(String message, UUID errorId, ErrorCode errorCode) {
        super(message);
        this.errorId = errorId;
        this.errorCode = errorCode;
    }

    public ValidationException(ErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        this.errorId = errorResponse.getErrorId();
        this.errorCode = errorResponse.getErrorCode();
        if (errorResponse.getErrors() != null) {
            withInfo(errorResponse.getErrors());
        }
    }

    public ValidationException withInfo(ErrorInfo info) {
        errors.add(info);
        return this;
    }

    public ValidationException withInfo(List<ErrorInfo> info) {
        errors.addAll(info);
        return this;
    }
}
