package com.nutri.rest.mapper;

import com.nutri.rest.model.Subscription;
import com.nutri.rest.model.User;
import com.nutri.rest.response.CustomerListResponse;
import com.nutri.rest.response.ItemDetailsResponse;
import lombok.experimental.UtilityClass;

import static com.nutri.rest.utils.AppUtils.*;

@UtilityClass
public class CustomerMapper {
    public CustomerListResponse mapFromUserDomainToResponse(User user, Subscription subscription){
        ItemDetailsResponse.LookupUnits lookupUnits = ItemDetailsResponse.LookupUnits.builder()
                .unitLookupCode(subscription!=null?subscription.getStatus().getLookupValueCode():"")
                .unitLookupValue(subscription!=null?subscription.getStatus().getLookupValue():"")
                .build();

        return CustomerListResponse
                .builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userName(user.getUserName())
                .phoneNumber(user.getPhoneNumber())
                .status(lookupUnits)
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
        ItemDetailsResponse.LookupUnits lookupUnits = ItemDetailsResponse.LookupUnits.builder()
                .unitLookupCode(castObjectToString(user[4]))
                .unitLookupValue(castObjectToString(user[5]))
                .build();

        return CustomerListResponse
                .builder()
                .firstName(castObjectToString(user[0]))
                .lastName(castObjectToString(user[1]))
                .userName(castObjectToString(user[2]))
                .phoneNumber(castObjectToString(user[3]))
                .status(lookupUnits)
                .customerInput(castObjectToString(user[6]))
                .dietitianInput(castObjectToString(user[7]))
                .subscriptionAmount(castObjectToBigDecimal(user[8]))
                .build();
    }


}
