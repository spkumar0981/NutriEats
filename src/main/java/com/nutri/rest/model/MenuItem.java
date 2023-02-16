package com.nutri.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints={ @UniqueConstraint(columnNames = {"subscriptionId", "itemId"}) })
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuItemId;

    @ManyToOne
    @JoinColumn(referencedColumnName = "subscriptionId", name = "subscriptionId")
    private Subscription subscriptionId;
    @ManyToOne
    @JoinColumn(referencedColumnName = "itemId", name = "itemId")
    private Item itemId;

    private Long quantity;

    private String isActive;
}
