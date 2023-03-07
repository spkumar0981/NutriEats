package com.nutri.rest.model;

import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints={ @UniqueConstraint(columnNames = {"fromUserId", "toUserId"}) })
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "fromUserId")
    private User fromUserId;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "toUserId")
    private User toUserId;

    @Column(nullable = false)
    private Long rating;

    @ManyToOne
    @JoinColumn(referencedColumnName = "lookupValueId", name = "commentCategory")
    private LookupValue commentCategory; //consulted for ??

    private String comments;

    private Boolean recommended;

    @ManyToMany(cascade = CascadeType.DETACH)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "RATING_COMMENT_OPTIONS", joinColumns = @JoinColumn(name = "RATING_ID", referencedColumnName = "ratingId"),
            inverseJoinColumns = @JoinColumn(name = "LOOKUP_VALUE_ID", referencedColumnName = "lookupValueId"))
    private Collection<LookupValue> commentOptions;
}
