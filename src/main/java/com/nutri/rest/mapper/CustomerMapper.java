package com.nutri.rest.mapper;

import com.nutri.rest.model.Subscription;
import com.nutri.rest.model.User;
import com.nutri.rest.response.CustomerListResponse;
import lombok.experimental.UtilityClass;

import static com.nutri.rest.utils.AppUtils.castObjectToString;

@UtilityClass
public class CustomerMapper {
    public CustomerListResponse mapFromUserDomainToResponse(User user, Subscription subscription){
        return CustomerListResponse
                .builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userName(user.getUserName())
                .phoneNumber(user.getPhoneNumber())
                .status(subscription!=null?subscription.getStatus().getLookupValueCode():"")
                .customerInput(subscription!=null?subscription.getCustomerInput():"")
                .dietitianInput(subscription!=null?subscription.getDietitianInput():"")
                .build();
    }

    public CustomerListResponse mapCustomerDetails(User user){
        return CustomerListResponse
                .builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userName(user.getUserName())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    public CustomerListResponse mapCustomerDetailsFromObjArray(Object[] user){

        return CustomerListResponse
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
