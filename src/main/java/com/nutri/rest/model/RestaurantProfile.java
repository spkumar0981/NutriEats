package com.nutri.rest.model;

import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class RestaurantProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String restaurantName;
    private int avgCost;
    @Column(length = 5000)
    private String bio;
    private String address;

    @Lob
    private byte[] restaurantImage;

    @ManyToMany(cascade = CascadeType.DETACH)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "RESTAURANT_CUISINES", joinColumns = @JoinColumn(name = "RESTAURANT_PROFILE_ID", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "LOOKUP_VALUE_ID", referencedColumnName = "lookupValueId"))
    private Collection<LookupValue> cuisines;
}
