package com.nutri.rest.request;

import com.nutri.rest.response.ItemDetailsResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {

    private String itemName;

    private String[] childItems;
    private Long quantity;

    private String instructions;
    private ItemDetailsResponse.LookupUnits quantityUnit;

}
