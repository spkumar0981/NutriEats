package com.nutri.rest.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.UUID;

import static com.nutri.rest.exception.ErrorCode.*;
import static com.nutri.rest.exception.ErrorResponse.*;
import static java.lang.String.format;


@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler{

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse exceptionHandler(final EntityNotFoundException ex) {
        UUID errorId = logException(ex, ex.getErrorId(), false);
        return notFound(errorId, ex.getErrorCode(), ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse exceptionHandler(final HttpMessageNotReadableException ex) {
        UUID errorId = logException(ex, true);
        return validation(errorId, M00G0001, ex.getLocalizedMessage());
    }

    @ExceptionHandler(EmailException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse exceptionHandler(final EmailException ex) {
        UUID errorId = logException(ex, true);
        return validation(errorId, M00G0002, ex.getLocalizedMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse exceptionHandler(ValidationException e) {
        UUID errorId = logException(e, false);
        return validation(errorId, M00G0001, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse exceptionHandler(IllegalArgumentException e) {
        UUID errorId = logException(e, false);
        return validation(errorId, M00G0001, e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse exceptionHandler(BindException e) {
        UUID errorId = logException(e, true);
        return validation(errorId, M00G0001).withInfo(e);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse exceptionHandler(ConstraintViolationException e) {
        UUID errorId = logException(e, false);
        return validation(errorId, M00G0001).withInfo(e);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse exceptionHandler(MissingServletRequestParameterException e) {
        UUID errorId = logException(e, true);
        return validation(errorId, M00G0001).withInfo(e);
    }

    @ExceptionHandler(EntityAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse exceptionHandler(EntityAlreadyExistException e) {
        UUID errorId = logException(e, false);
        return alreadyExist(errorId, M00G0001, HttpStatus.CONFLICT, e.getLocalizedMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse exceptionHandler(MethodArgumentNotValidException ex) {
        UUID errorId = logException(ex, true);
        return validation(errorId, M00G0001).withInfo(ex);
    }


    @ExceptionHandler(AuthorizationServiceException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse exceptionHandler(AuthorizationServiceException ex) {
        UUID errorId = logException(ex, false);
        return authorization(errorId, M00G0003, ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse exceptionHandler(AccessDeniedException ex) {
        UUID errorId = logException(ex, false);
        return authorization(errorId, M00G0003, ex.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse exceptionHandler(ForbiddenException ex) {
        UUID errorId = logException(ex, false);
        return authorization(errorId, M00G0003, ex.getMessage());
    }

    @ExceptionHandler(com.nutri.rest.exception.ValidationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse validationException(final Exception ex) {
        UUID errorId = logException(ex, true);
        return server(errorId, M00G0002, ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse generalExceptionHandler(final Exception ex) {
        UUID errorId = logException(ex, true);
        return server(errorId, M00G0002, "Internal Server Error, kindly contact administrator");
    }

    private <E extends Throwable> UUID logException(E exception, boolean printTrace) {
        return logException(exception, UUID.randomUUID(), printTrace);
    }

    private <E extends Throwable> UUID logException(E exception, UUID errorId, boolean printTrace) {
        String message =
                format(
                        "%s Caught - Error id %s. %s",
                        exception.getClass().getSimpleName(), errorId, exception.getMessage());
        if (printTrace) log.error(message, exception);
        else log.error(message);
        return errorId;
    }
}
