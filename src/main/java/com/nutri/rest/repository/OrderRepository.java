package com.nutri.rest.repository;

import com.nutri.rest.model.LookupValue;
import com.nutri.rest.model.Order;
import com.nutri.rest.model.User;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerIdAndOrderStatusIdNot(User customer, LookupValue orderStatusId);

    List<Order> findByCustomerId(User customer);
    List<Order> findByRestaurantId(User restaurant);
    List<Order> findByRestaurantIdAndOrderStatusIdNot(User customer, LookupValue orderStatusId);
    List<Order> findByCustomerIdAndDietitianId(User customer, User dietitian);

    @Query(value = "SELECT COUNT(*) FROM ORDERS ORD " +
            "INNER JOIN ORDER_ITEMS OI ON (OI.ORDER_ID=ORD.ORDER_ID) " +
            "WHERE ORD.RESTAURANT_ID=?1 AND OI.RECURRING_ORDER_ID=?2 AND ORD.ORDER_DATE>=?3 AND ORD.ORDER_DATE<=?4", nativeQuery = true)
    Long findIfOrderCreatedForTheDay(Long restaurantId, Long orderId, LocalDateTime orderDate1, LocalDateTime orderDate2);
}
