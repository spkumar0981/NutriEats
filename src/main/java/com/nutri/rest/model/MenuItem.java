package com.nutri.rest.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints={ @UniqueConstraint(columnNames = {"subscriptionId", "parentItemId"}) })
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuItemId;

    @ManyToOne
    @JoinColumn(referencedColumnName = "subscriptionId", name = "subscriptionId")
    private Subscription subscriptionId;
    @ManyToOne
    @JoinColumn(referencedColumnName = "itemId", name = "parentItemId")
    private ParentItem parentItemId;

    private String childItems;

    private Long quantity;

    private String instructions;

    private String isActive;
    @ManyToOne
    @JoinColumn(referencedColumnName = "lookupValueId", name = "quantityUnit", nullable = false)
    private LookupValue quantityUnit;
}
