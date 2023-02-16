package com.nutri.rest.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class ValidateOTPRequest {

    @NotEmpty(message = "Username is required")
    @Pattern(regexp = "^[A-Za-z0-9]+$",message = "The given username is invalid because it has invalid characters")
    private String userName;

    @NotEmpty
    private String otp;

}