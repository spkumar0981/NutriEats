package com.nutri.rest.mapper;

import com.nutri.rest.model.Subscription;
import com.nutri.rest.model.User;
import com.nutri.rest.response.CustomerListResponse;
import com.nutri.rest.response.DietitianListResponse;
import lombok.experimental.UtilityClass;

import static com.nutri.rest.utils.AppUtils.castObjectToString;

@UtilityClass
public class DietitianMapper {
    public DietitianListResponse mapFromUserDomainToResponse(User user, Subscription subscription){
        return DietitianListResponse
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

    public DietitianListResponse mapDietitianDetailsFromObjArray(Object[] user){

        return DietitianListResponse
                .builder()
                .firstName(castObjectToString(user[0]))
                .lastName(castObjectToString(user[1]))
                .userName(castObjectToString(user[2]))
                .phoneNumber(castObjectToString(user[3]))
                .status(castObjectToString(user[4]))
                .customerInput(castObjectToString(user[5]))
                .dietitianInput(castObjectToString(user[6]))
                .build();
    }
}