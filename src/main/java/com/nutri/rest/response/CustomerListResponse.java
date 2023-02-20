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
public class CustomerListResponse {

    private String firstName;
    private String lastName;
    private String userName;
    private String phoneNumber;
    private String status;
    private String customerInput;
    private String dietitianInput;

}
