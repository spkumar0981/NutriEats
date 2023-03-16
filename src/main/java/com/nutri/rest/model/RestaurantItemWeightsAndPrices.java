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
@Table(uniqueConstraints={ @UniqueConstraint(columnNames = {"restaurantItemId", "quantity", "quantityUnit"}) })
public class RestaurantItemWeightsAndPrices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantWeightItemId;
    @ManyToOne
    @JoinColumn(referencedColumnName = "restaurantItemId", name = "restaurantItemId")
    private RestaurantItems restaurantItemId;
    private BigDecimal itemPrice;
    private Long quantity;
    @ManyToOne
    @JoinColumn(referencedColumnName = "lookupValueId", name = "quantityUnit", nullable = false)
    private LookupValue quantityUnit;
}
