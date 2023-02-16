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
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column(unique = true)
    private String itemName;
    private String itemDescription;
    @Lob
    private byte[] itemImage;
    private String itemCategory;
    @ManyToOne
    @JoinColumn(referencedColumnName = "lookupValueId", name = "itemUnit")
    private LookupValue itemUnit;
}
