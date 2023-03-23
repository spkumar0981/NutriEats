package com.nutri.rest.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@Table(name = "RECURRING_ORDERS")
public class RecurringOrders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private Long orderNumber;

    @ManyToOne
    @JoinColumn(referencedColumnName = "menuItemId", name = "menuItemId")
    private MenuItem menuItemId;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "restaurantId")
    private User restaurantId;
    private ZonedDateTime fromDate;
    private ZonedDateTime toDate;
    private String orderDeliveryTime;
    private BigDecimal orderTotalPrice;

    @ManyToOne
    @JoinColumn(referencedColumnName = "lookupValueId", name = "orderStatus")
    private LookupValue orderStatus;

    private String deliveryAddress;
}
