package com.nutri.rest.repository;

import com.nutri.rest.model.Item;
import com.nutri.rest.model.MenuItem;
import com.nutri.rest.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findBySubscriptionId(Subscription subscription);

    MenuItem findBySubscriptionIdAndItemId(Subscription subscription, Item item);
}