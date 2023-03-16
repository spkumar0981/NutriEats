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
@Table(uniqueConstraints={ @UniqueConstraint(columnNames = {"restaurantId", "childItemId"}) })
public class RestaurantItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantItemId;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "restaurantId")
    private User restaurantId;
    @ManyToOne
    @JoinColumn(referencedColumnName = "itemId", name = "childItemId")
    private ChildItem childItemId;
    @Lob
    private byte[] itemImage;
    private String availableFromTime;
    private String availableToTime;
    private String itemDescription;
    private String isActive; //if the item is exhausted for the day, then we can use this flag to deactivate it
}
