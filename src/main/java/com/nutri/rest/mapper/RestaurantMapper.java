package com.nutri.rest.mapper;

import com.nutri.rest.model.RestaurantItems;
import com.nutri.rest.model.User;
import com.nutri.rest.response.RestaurantItemsResponse;
import com.nutri.rest.response.ItemDetailsResponse;
import com.nutri.rest.response.RestaurantListResponse;
import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.stream.Collectors;

@UtilityClass
public class RestaurantMapper {


    public RestaurantListResponse mapFromUserDomainToResponse(User user){
        return RestaurantListResponse
                .builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .restaurantName(user.getRestaurantProfile()!=null?user.getRestaurantProfile().getRestaurantName():null)
                .userName(user.getUserName())
                .phoneNumber(user.getPhoneNumber())
                .avgCost(user.getRestaurantProfile()!=null?user.getRestaurantProfile().getAvgCost():-1)
                .cuisines(user.getRestaurantProfile()!=null?user.getRestaurantProfile().getCuisines()
                        .stream().map(lookupValue -> ItemDetailsResponse.LookupUnits.builder()
                                .unitLookupCode(lookupValue.getLookupValueCode())
                                .unitLookupValue(lookupValue.getLookupValue()).build()).collect(Collectors.toList()):null)
                .restaurantImage(user.getRestaurantProfile()!=null ? user.getRestaurantProfile().getRestaurantImage()!=null ?
                        new String(user.getRestaurantProfile().getRestaurantImage()) : null : null)
                .build();
    }

}
