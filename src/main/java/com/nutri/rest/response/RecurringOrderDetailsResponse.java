package com.nutri.rest.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RecurringOrderDetailsResponse {
    private String itemName;
    private String childItems;
    private String quantityAndUnit;
    private String instructions;
    private ZonedDateTime fromDate;
    private ZonedDateTime toDate;
    private String deliveryTime;
    private String orderId;
    private BigDecimal price;

    private String restaurantName;
}
