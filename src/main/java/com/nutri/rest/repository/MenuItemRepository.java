package com.nutri.rest.repository;

import com.nutri.rest.model.ParentItem;
import com.nutri.rest.model.MenuItem;
import com.nutri.rest.model.Subscription;
import com.nutri.rest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findBySubscriptionId(Subscription subscription);

    MenuItem findBySubscriptionIdAndParentItemId(Subscription subscription, ParentItem parentItem);

    @Query(value = "SELECT MI.* FROM MENU_ITEM MI " +
            "INNER JOIN SUBSCRIPTION S ON (S.SUBSCRIPTION_ID=MI.SUBSCRIPTION_ID) " +
            "WHERE S.CUSTOMER_ID=?1 AND S.DIETITIAN_ID=?2 AND MI.PARENT_ITEM_ID=?3", nativeQuery = true)
    MenuItem getMenuItem(long customerId, long dietitianId, long parentItemId);
}
