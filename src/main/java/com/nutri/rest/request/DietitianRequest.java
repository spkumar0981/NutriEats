package com.nutri.rest.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DietitianRequest {

    private String firstName;
    private String lastName;
    private String userName;
    private String phoneNumber;

    private String customerInput;
    private String dietitianInput;

}
