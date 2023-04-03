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
public class RestaurantListResponse {

    private String firstName;

    private String lastName;

    private String restaurantName;
    private String userName;
    private String phoneNumber;
    private int avgCost;
    private List<ItemDetailsResponse.LookupUnits> cuisines;

    private Double rating;

    private String restaurantImage;

}
