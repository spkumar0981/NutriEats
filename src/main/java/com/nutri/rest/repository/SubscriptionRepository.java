package com.nutri.rest.repository;

import com.nutri.rest.model.Subscription;
import com.nutri.rest.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Subscription findByCustomerIdAndDietitianId(User customerId, User dietitianId);

    @Query(value = "SELECT U.*, (SELECT STATUS) FROM USER U " +
            "INNER JOIN USER_ROLE UR ON (UR.USER_ID=U.ID) " +
            "INNER JOIN ROLE R ON (UR.ROLE_ID=R.ID) " +
            "WHERE R.CODE_NAME = ?1",
            countQuery = "SELECT * FROM USER U " +
                    "INNER JOIN USER_ROLE UR ON (UR.USER_ID=U.ID) " +
                    "INNER JOIN ROLE R ON (UR.ROLE_ID=R.ID) " +
                    "WHERE R.CODE_NAME = ?1",
            nativeQuery = true)
    Page<User> findByUserType(String lastname, Pageable pageable);
}
