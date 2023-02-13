package com.nutri.rest.exception;

/**
 * Error code pattern is MXXYZZZZ, where:
 *
 * <p>M : static letter XX : microservice id: - 00 : UNKNOWN, - 01 : BANK_MANAGEMENT, - 02 :
 * CALENDAR, - 03 : CUSTOMER_MANAGEMENT, - 04 : FILE_MANAGEMENT, - 05 : LEDGERS, - 06 :
 * NOTIFICATION, - 07 : ORDERING, - 08 : PRODUCT_MANAGEMENT, - 09 : PRODUCT_OFFERING, - 10 :
 * USER_MANAGEMENT, - 11 : PISP_TOKENIO, - 12 : TRANSIT_ENTITY, Y : G - generic, D - defined ZZZZ:
 * error number
 */
public enum ErrorCode {

    // generic error codes
    M00G0000("Unknown exception."),
    M00G0001("Validation exception."),
    M00G0002("Internal server error."),
    M00G0003("Authorization forbidden."),
    M00G0004("Entity not found."),

    // transit entity exceptions
    M12G0000("Generic exception."),
    M12D1001("Duplicate payment exception. Payment with given transaction id already exists."),
    M12D1002("Error while parsing http response body to json."),
    ;

    private String description;

    ErrorCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
