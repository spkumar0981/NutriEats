package com.nutri.rest.model;

import lombok.*;
import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class OrderItems {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderItemId;
    @ManyToOne
    @JoinColumn(referencedColumnName = "orderId", name = "orderId")
    private Order orderId;

    private Long quantity;
    @ManyToOne
    @JoinColumn(referencedColumnName = "itemId", name = "childItemId")
    private ChildItem childItem;
    @ManyToOne
    @JoinColumn(referencedColumnName = "restaurantWeightItemId", name = "itemWeightsAndPricesId")
    private RestaurantItemWeightsAndPrices itemWeightsAndPrices;

    @ManyToOne
    @JoinColumn(referencedColumnName = "orderId", name = "recurringOrderId")
    private RecurringOrders recurringOrderId;
}
