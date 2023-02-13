package com.nutri.rest.request;

import lombok.Data;

@Data
public class UpdateUserPwdRequest {

    /*@NotEmpty(message = "Username is required")
    @Pattern(regexp = "^[A-Za-z0-9]+$",message = "The given username is invalid because it has invalid characters")
    private String username;*/

    private String password;
}
