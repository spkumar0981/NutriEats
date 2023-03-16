package com.nutri.rest.repository;

import com.nutri.rest.model.ChildItem;
import com.nutri.rest.model.RestaurantItems;
import com.nutri.rest.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantItemsRepository extends JpaRepository<RestaurantItems, Long> {
    List<RestaurantItems> findByRestaurantId(User user);
    RestaurantItems findByRestaurantIdAndChildItemId(User user, ChildItem childItem);
}
