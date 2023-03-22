package com.nutri.rest.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RecurringOrderResponse {
    private String orderId;
    private String customerName;

    private String customerUsername;

    private String customerAddress;
    private String restaurantName;
    private String dietitianName;
    private String orderStatus;
    private String orderStatusCode;
}
