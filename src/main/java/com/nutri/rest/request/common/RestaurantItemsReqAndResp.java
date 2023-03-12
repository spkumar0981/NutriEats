package com.nutri.rest.request.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantItemsReqAndResp {
    private String restaurantUserName;
    private String itemName;
    private String availableFromTime;
    private String availableToTime;
    private BigDecimal itemPrice;
    private String itemDescription;
    private String isActive;
    private byte[] itemImage;
    private String itemCategory;
    private String parentItemName;
}
