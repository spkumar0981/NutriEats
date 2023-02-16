package com.nutri.rest.mapper;

import com.nutri.rest.model.User;
import com.nutri.rest.request.CreateUserRequest;
import com.nutri.rest.response.UserResponse;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;

@UtilityClass
public class UserMapper {


    public UserResponse mapFromUserDomainToResponse(User user){
        return UserResponse
                .builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userName(user.getUserName())
                .phoneNumber(user.getPhoneNumber())
                .price(user.getBasePrice())
                .build();
    }

    public User mapFromUserRequestToDomain(String encryptedPassword, CreateUserRequest createUserRequest){
        return User
                .builder()
                .firstName(createUserRequest.getFirstName())
                .lastName(createUserRequest.getLastName())
                .userName(createUserRequest.getUserName())
                .password(encryptedPassword)
                .phoneNumber(createUserRequest.getPhoneNumber())
                .passwordUpdateDate(LocalDate.now())
                .build();
    }

    public UserResponse mapFromUserDomainToResponseFilteringRBSRole(User user){
        return UserResponse
                .builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userName(user.getUserName())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

}
