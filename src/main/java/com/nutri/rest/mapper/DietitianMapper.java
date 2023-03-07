package com.nutri.rest.mapper;

import com.nutri.rest.model.Subscription;
import com.nutri.rest.model.User;
import com.nutri.rest.response.CustomerListResponse;
import com.nutri.rest.response.DietitianListResponse;
import com.nutri.rest.response.ItemDetailsResponse;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

import static com.nutri.rest.utils.AppUtils.castObjectToBigDecimal;
import static com.nutri.rest.utils.AppUtils.castObjectToString;

@UtilityClass
public class DietitianMapper {
    public DietitianListResponse mapFromUserDomainToResponse(User user, Subscription subscription, Double dietitianRating){
        ItemDetailsResponse.LookupUnits lookupUnits = ItemDetailsResponse.LookupUnits.builder()
                .unitLookupCode(subscription!=null?subscription.getStatus().getLookupValueCode():"")
                .unitLookupValue(subscription!=null?subscription.getStatus().getLookupValue():"")
                .build();
        ItemDetailsResponse.LookupUnits preferredMealOption = ItemDetailsResponse.LookupUnits.builder()
                .unitLookupCode(subscription!=null?subscription.getPreferredMealOption()!=null?subscription.getPreferredMealOption().getLookupValueCode():"":"")
                .unitLookupValue(subscription!=null?subscription.getPreferredMealOption()!=null?subscription.getPreferredMealOption().getLookupValue():"":"")
                .build();

        return DietitianListResponse
                .builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userName(user.getUserName())
                .phoneNumber(user.getPhoneNumber())
                .price(user.getBasePrice())
                .subscriptionAmount(subscription!=null?subscription.getAmount():BigDecimal.ZERO)
                .status(lookupUnits)
                .customerInput(subscription!=null?subscription.getCustomerInput():"")
                .dietitianInput(subscription!=null?subscription.getDietitianInput():"")
                .preferredMealOption(preferredMealOption)
                .rating(dietitianRating)
                .build();
    }

    public DietitianListResponse mapDietitianDetailsFromObjArray(Object[] user){
        ItemDetailsResponse.LookupUnits lookupUnits = ItemDetailsResponse.LookupUnits.builder()
                .unitLookupCode(castObjectToString(user[4]))
                .unitLookupValue(castObjectToString(user[5]))
                .build();

        ItemDetailsResponse.LookupUnits preferredMeal = ItemDetailsResponse.LookupUnits.builder()
                .unitLookupCode(castObjectToString(user[9]))
                .unitLookupValue(castObjectToString(user[10]))
                .build();

        return DietitianListResponse
                .builder()
                .firstName(castObjectToString(user[0]))
                .lastName(castObjectToString(user[1]))
                .userName(castObjectToString(user[2]))
                .phoneNumber(castObjectToString(user[3]))
                .status(lookupUnits)
                .customerInput(castObjectToString(user[6]))
                .dietitianInput(castObjectToString(user[7]))
                .subscriptionAmount(castObjectToBigDecimal(user[8]))
                .preferredMealOption(preferredMeal)
                .build();
    }
}
