package com.nutri.rest.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@Table(name = "ORDERS")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "customerId")
    private User customerId;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "restaurantId")
    private User restaurantId;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "dietitianId")
    private User dietitianId;

    @ManyToOne
    @JoinColumn(referencedColumnName = "lookupValueId", name = "orderStatusId")
    private LookupValue orderStatusId;

    private BigDecimal orderTotalPrice;

    private String deliveryAddress;
}
