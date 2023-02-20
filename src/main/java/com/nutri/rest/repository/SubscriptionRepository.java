package com.nutri.rest.repository;

import com.nutri.rest.model.Subscription;
import com.nutri.rest.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Subscription findByCustomerIdAndDietitianId(User customerId, User dietitianId);
    Subscription findByCustomerId(User customerId);
    Subscription findByDietitianId(User dietitianId);
}
