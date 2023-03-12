package com.nutri.rest.request;

import lombok.*;

import javax.validation.constraints.Email;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    @Email
    private String userName;

    @NonNull
    private String userType;

    @NonNull
    private String password;
    @NonNull
    private String phoneNumber;

    private String restaurantName;
}
