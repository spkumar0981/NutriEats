package com.nutri.rest.request;

import com.nutri.rest.model.LookupValue;
import com.nutri.rest.response.ItemDetailsResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantItemsRequest {
    private String restaurantUserName;
    private String parentItemName;
    private String itemName;
    private String availableFromTime;
    private String availableToTime;
    private BigDecimal itemPrice;
    private String itemDescription;
    private String isActive;
    private String itemImage;
    private String itemCategory;
    private Long quantity;
    private ItemDetailsResponse.LookupUnits quantityUnit;
}