package com.nutri.rest.mapper;

import com.nutri.rest.model.Subscription;
import com.nutri.rest.model.User;
import com.nutri.rest.response.DietitansListResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DietitianMapper {
    public DietitansListResponse mapFromUserDomainToResponse(User user, Subscription subscription){
        return DietitansListResponse
                .builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userName(user.getUserName())
                .phoneNumber(user.getPhoneNumber())
                .price(user.getBasePrice())
                .status(subscription!=null?subscription.getStatus().getLookupValueCode():"")
                .customerInput(subscription!=null?subscription.getCustomerInput():"")
                .dietitianInput(subscription!=null?subscription.getDietitianInput():"")
                .build();
    }
}
