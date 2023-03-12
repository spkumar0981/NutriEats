package com.nutri.rest.mapper;

import com.nutri.rest.model.DietitianExperienceDetails;
import com.nutri.rest.model.DietitianRecognitions;
import com.nutri.rest.model.User;
import com.nutri.rest.request.CreateUserRequest;
import com.nutri.rest.request.ExperienceDetailsRequestAndResponse;
import com.nutri.rest.request.RecognitionsRequestAndResponse;
import com.nutri.rest.response.ItemDetailsResponse;
import com.nutri.rest.response.RestaurantListResponse;
import com.nutri.rest.response.UserResponse;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
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
                .build();
    }

}
