package com.nutri.rest.exception;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.nutri.rest.exception.ErrorCode.M00G0003;


@Getter
public class ForbiddenException extends RuntimeException {
    private final UUID errorId;
    private final ErrorCode errorCode;
    private final List<ErrorInfo> errors = new ArrayList<>();

    public ForbiddenException(String message) {
        this(message, UUID.randomUUID(), M00G0003);
    }

    public ForbiddenException(String message, UUID errorId, ErrorCode errorCode) {
        super(message);
        this.errorId = errorId;
        this.errorCode = errorCode;
    }

    public ForbiddenException(ErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        this.errorId = errorResponse.getErrorId();
        this.errorCode = errorResponse.getErrorCode();
        if (errorResponse.getErrors() != null) {
            withInfo(errorResponse.getErrors());
        }
    }

    public ForbiddenException withInfo(ErrorInfo info) {
        errors.add(info);
        return this;
    }

    public ForbiddenException withInfo(List<ErrorInfo> info) {
        errors.addAll(info);
        return this;
    }
}
