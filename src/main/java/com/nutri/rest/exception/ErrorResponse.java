package com.nutri.rest.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private UUID errorId;
    private ErrorCode errorCode;
    private HttpStatus status;
    private String message;
    private List<ErrorInfo> errors = new ArrayList<>();

    private ErrorResponse(UUID errorId, ErrorCode errorCode, HttpStatus status, String message) {
        this.errorId = errorId;
        this.errorCode = errorCode;
        this.status = status;
        this.message = message;
    }

    public static ErrorResponse validation(UUID errorId, ErrorCode errorCode, String message) {
        return new ErrorResponse(errorId, errorCode, HttpStatus.BAD_REQUEST, message);
    }

    public static ErrorResponse validation(UUID errorId, ErrorCode errorCode) {
        return validation(errorId, errorCode, null);
    }

    public static ErrorResponse server(UUID errorId, ErrorCode errorCode, String message) {
        return new ErrorResponse(errorId, errorCode, HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public static ErrorResponse authorization(UUID errorId, ErrorCode errorCode, String message) {
        return new ErrorResponse(errorId, errorCode, HttpStatus.FORBIDDEN, message);
    }

    public static ErrorResponse notFound(UUID errorId, ErrorCode errorCode, String message) {
        return new ErrorResponse(errorId, errorCode, HttpStatus.NOT_FOUND, message);
    }

    public static ErrorResponse of(
            UUID errorId, ErrorCode errorCode, HttpStatus status, String message) {
        return new ErrorResponse(errorId, errorCode, status, message);
    }

    public static ErrorResponse alreadyExist(
            UUID errorId, ErrorCode errorCode, HttpStatus status, String message) {
        return new ErrorResponse(errorId, errorCode, status, message);
    }

    public ErrorResponse withInfo(ErrorInfo info) {
        errors.add(info);
        return this;
    }

    public ErrorResponse withInfo(List<ErrorInfo> info) {
        errors.addAll(info);
        return this;
    }

    public ErrorResponse withInfo(ConstraintViolationException ex) {
        if (ex.getConstraintViolations().size() == 0) {
            return this;
        }
        //ex.getConstraintViolations().stream().map(ErrorInfo::of).forEach(errors::add);
        return this;
    }

    public ErrorResponse withInfo(MethodArgumentNotValidException ex) {
        if (ex.getBindingResult().getAllErrors().size() == 0) {
            return this;
        }
        ex.getBindingResult().getAllErrors().stream().map(ErrorInfo::of).forEach(errors::add);
        return this;
    }

    public ErrorResponse withInfo(MissingServletRequestParameterException ex) {
        errors.add(ErrorInfo.of(ex.getParameterName(), null, "required parameter is not present"));
        return this;
    }

    public ErrorResponse withInfo(BindException ex) {
        ex.getAllErrors().stream()
                .map(
                        error -> {
                            ErrorInfo errorInfo;
                            if (error instanceof FieldError) {
                                FieldError fieldError = (FieldError) error;
                                errorInfo =
                                        ErrorInfo.of(
                                                fieldError.getField(),
                                                fieldError.getRejectedValue(),
                                                fieldError.getDefaultMessage());
                            } else {
                                errorInfo = ErrorInfo.of(error.getObjectName(), null, error.getDefaultMessage());
                            }
                            return errorInfo;
                        })
                .forEach(errors::add);
        return this;
    }
}
