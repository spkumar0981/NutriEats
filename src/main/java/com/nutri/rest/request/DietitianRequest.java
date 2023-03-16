package com.nutri.rest.request;

import com.nutri.rest.response.ItemDetailsResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DietitianRequest {

    private String firstName;
    private String lastName;
    private String userName;
    private String phoneNumber;

    private BigDecimal price;

    private BigDecimal subscriptionAmount;

    private String customerInput;
    private String dietitianInput;

    private ItemDetailsResponse.LookupUnits preferredMealOption;

    private String[] allergens;

}
