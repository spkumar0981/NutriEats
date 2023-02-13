package com.nutri.rest.exception;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.nutri.rest.exception.ErrorCode.M00G0004;


@Getter
public class EntityNotFoundException extends RuntimeException {

    private final UUID errorId;
    private final ErrorCode errorCode;
    private final List<ErrorInfo> errors = new ArrayList<>();

    public EntityNotFoundException(String message) {
        this(message, UUID.randomUUID(), M00G0004);
    }

    public EntityNotFoundException(String message, UUID errorId, ErrorCode errorCode) {
        super(message);
        this.errorId = errorId;
        this.errorCode = errorCode;
    }

    public EntityNotFoundException(ErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        this.errorId = errorResponse.getErrorId();
        this.errorCode = errorResponse.getErrorCode();
        if (errorResponse.getErrors() != null) {
            withInfo(errorResponse.getErrors());
        }
    }

    public EntityNotFoundException withInfo(ErrorInfo info) {
        errors.add(info);
        return this;
    }

    public EntityNotFoundException withInfo(List<ErrorInfo> info) {
        errors.addAll(info);
        return this;
    }
}
