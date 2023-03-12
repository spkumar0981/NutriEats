package com.nutri.rest.repository;

import com.nutri.rest.model.RestaurantProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantProfileRepository extends JpaRepository<RestaurantProfile, Long> {
    RestaurantProfile findByRestaurantName(String restaurantName);
}
