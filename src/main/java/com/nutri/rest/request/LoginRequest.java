package com.nutri.rest.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginRequest {

    @NotEmpty(message = "Username is mandatory")
    private String userName;

    @NotEmpty(message = "Type of user is mandatory")
    private String userType;

    @NotEmpty(message = "password is mandatory")
    private String password;

    private int captchaId;
    private String captchaResponse;
}