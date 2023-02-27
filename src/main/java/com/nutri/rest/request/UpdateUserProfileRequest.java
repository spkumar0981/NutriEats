package com.nutri.rest.request;

import com.nutri.rest.model.Role;
import lombok.Data;

import javax.validation.constraints.Email;
import java.util.List;

@Data
public class UpdateUserProfileRequest {

    @Email
    private String userName;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String price;

}
