package com.nutri.rest.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private String orderId;
    private String customerName;
    private String deliveryAddress;
    private String restaurantName;
    private String dietitianName;
    private String orderStatus;
    private String orderStatusCode;
    private List<OrderItemResponse> orderItems;
    private BigDecimal orderTotalPrice;
}
