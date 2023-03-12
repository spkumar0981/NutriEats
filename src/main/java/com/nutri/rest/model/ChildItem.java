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
    @Lob
    private byte[] itemImage;
    private String itemCategory;

    private Long lookupValueTypeOfItemUnit;

    @ManyToOne
    @JoinColumn(referencedColumnName = "itemId", name = "parentItemId")
    private ParentItem parentItem;
}
