package com.nutri.rest.mapper;

import com.nutri.rest.model.Item;
import com.nutri.rest.model.LookupValue;
import com.nutri.rest.model.MenuItem;
import com.nutri.rest.response.ItemDetailsResponse;
import com.nutri.rest.response.ItemResponse;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.nutri.rest.utils.AppUtils.castObjectToString;
import static com.nutri.rest.utils.AppUtils.castObjectToStringArray;

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
