package com.nutri.rest.mapper;

import com.nutri.rest.model.LookupValue;
import com.nutri.rest.response.ItemDetailsResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LookupMapper {
    public ItemDetailsResponse.LookupUnits mapToLookups(LookupValue lookupValue){
        return ItemDetailsResponse.LookupUnits
                .builder()
                .unitLookupCode(lookupValue.getLookupValueCode())
                .unitLookupValue(lookupValue.getLookupValue())
                .build();
    }
}
