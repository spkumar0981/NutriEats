package com.nutri.rest.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginRequest {

    @NotEmpty(message = "Username is mandatory")
    private String username;

    @NotEmpty(message = "password is mandatory")
    private String password;

    private int captchaId;
    private String captchaResponse;
}