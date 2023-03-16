package com.nutri.rest.repository;

import com.nutri.rest.model.LookupValue;
import com.nutri.rest.model.RestaurantItemWeightsAndPrices;
import com.nutri.rest.model.RestaurantItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantItemWeightsAndPricesRepository extends JpaRepository<RestaurantItemWeightsAndPrices, Long> {

    RestaurantItemWeightsAndPrices findByRestaurantItemIdAndQuantityAndQuantityUnit(RestaurantItems restaurantItemId, Long quantity, LookupValue quantityUnit);
    List<RestaurantItemWeightsAndPrices> findByRestaurantItemId(RestaurantItems restaurantItemId);
}
