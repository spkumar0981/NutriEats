package com.nutri.rest.request;

import com.nutri.rest.model.Role;
import lombok.Data;

import javax.validation.constraints.Email;
import java.util.List;

@Data
public class UpdateUserRequest {

    @Email
    private String email;

    private List<Role> roles;

    private String password;

    private String phoneNumber;

}
