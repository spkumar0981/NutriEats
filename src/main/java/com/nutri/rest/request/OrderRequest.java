package com.nutri.rest.request;

import com.nutri.rest.utils.ItemWeightsAndPrices;
import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private String parentItemName;
    private String childItemName;
    private ItemWeightsAndPrices itemWeightsAndPrices;
    private Long quantity;
    private String recurringOrderId;
}
