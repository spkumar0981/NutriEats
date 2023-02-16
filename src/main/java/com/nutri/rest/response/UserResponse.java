package com.nutri.rest.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private String firstName;
    private String lastName;
    private String userName;
    private String phoneNumber;

    private BigDecimal price;

}
