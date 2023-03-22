package com.nutri.rest.utils;

import com.nutri.rest.response.ItemDetailsResponse;
import lombok.*;

import java.math.BigDecimal;
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ItemWeightsAndPrices {
    private BigDecimal itemPrice;
    private Long quantity;
    private ItemDetailsResponse.LookupUnits quantityUnit;
}
