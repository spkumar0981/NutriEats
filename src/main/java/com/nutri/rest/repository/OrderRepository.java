package com.nutri.rest.repository;

import com.nutri.rest.model.Order;
import com.nutri.rest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerId(User customer);
    List<Order> findByRestaurantId(User customer);
    List<Order> findByCustomerIdAndDietitianId(User customer, User dietitian);
}
