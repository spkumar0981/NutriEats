package com.nutri.rest.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LookupValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lookupValueId;
    private Long lookupValueType;
    @Column(unique = true)
    private String lookupValueCode;
    private String lookupValue;

    private String lookupCategory;
}
