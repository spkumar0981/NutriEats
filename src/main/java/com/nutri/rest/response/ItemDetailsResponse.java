package com.nutri.rest.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDetailsResponse {

    private String itemName;

    private List<LookupUnits> itemUnitsAndCodes;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LookupUnits{
        private String unitLookupCode;
        private String unitLookupValue;
    }

}

