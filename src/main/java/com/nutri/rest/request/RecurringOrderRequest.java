package com.nutri.rest.request;

import com.nutri.rest.utils.ItemWeightsAndPrices;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.Date;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RecurringOrderRequest {
    private String itemName;
    private String childItems;
    private Long quantity;
    private String quantityUnit;
    private String instructions;
    private String restaurant; ///username of restaurant
    private ZonedDateTime fromDate;
    private ZonedDateTime toDate;
    private String deliveryTime;
    private String orderId;

    private String price;
}
