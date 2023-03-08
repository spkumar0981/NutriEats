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
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private int overallExperience;
    private int specialistExperience;

    @ManyToOne
    @JoinColumn(referencedColumnName = "lookupValueId", name = "qualifiedDegree")
    private LookupValue qualifiedDegree;

    private String degreeUniversity;
    private Long degreeYear;

    @Column(length = 5000)
    private String bio;
    private String address;

    @ManyToMany(cascade = CascadeType.DETACH)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "USER_SERVICES", joinColumns = @JoinColumn(name = "USER_PROFILE_ID", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "LOOKUP_VALUE_ID", referencedColumnName = "lookupValueId"))
    private Collection<LookupValue> services;
}
