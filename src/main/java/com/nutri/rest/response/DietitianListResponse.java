package com.nutri.rest.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DietitianListResponse {

    private String firstName;
    private String lastName;
    private String userName;
    private String phoneNumber;

    private BigDecimal subscriptionAmount;
    private BigDecimal price;
    private ItemDetailsResponse.LookupUnits status;
    private ItemDetailsResponse.LookupUnits preferredMealOption;

    private String customerInput;
    private String dietitianInput;

    private Double rating;

}
