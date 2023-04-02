package com.nutri.rest.response;

import com.nutri.rest.utils.ItemWeightsAndPrices;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
        private String itemImage;
        private String itemCategory;
        private List<ItemWeightsAndPrices> itemWeightsAndPrices;
    }
}
