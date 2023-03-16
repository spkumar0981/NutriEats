package com.nutri.rest.response;

import com.nutri.rest.model.LookupValue;
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
public class RestaurantItemsResponse {
    private String restaurantUserName;
    private String parentItemName;

    private List<ChildItems> childItems;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChildItems{
        private String itemName;
        private String availableFromTime;
        private String availableToTime;
        private String itemDescription;
        private String isActive;
        private byte[] itemImage;
        private String itemCategory;
        private List<ItemWeightsAndPrices> itemWeightsAndPrices;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemWeightsAndPrices{
        private BigDecimal itemPrice;
        private Long quantity;
        private ItemDetailsResponse.LookupUnits quantityUnit;
    }
}
