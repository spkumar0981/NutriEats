package com.nutri.rest.response;

import com.nutri.rest.utils.ItemWeightsAndPrices;
import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
    private Long quantity;
    private String childItemName;
    private String parentItemName;
    private ItemWeightsAndPrices itemWeightsAndPrices;
}
