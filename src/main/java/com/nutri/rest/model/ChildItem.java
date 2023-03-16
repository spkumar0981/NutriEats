package com.nutri.rest.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChildItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column(unique = true)
    private String itemName;
    private String itemCategory;

//    private Long lookupValueTypeOfItemUnit; //weights are maintained in restaurant menu table itself

    @ManyToOne
    @JoinColumn(referencedColumnName = "itemId", name = "parentItemId")
    private ParentItem parentItem;
}
