package com.nutri.rest.repository;

import com.nutri.rest.model.RecurringOrders;
import com.nutri.rest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;

public interface RecurringOrderRepository extends JpaRepository<RecurringOrders, Long> {
    @Query(value = "SELECT MAX(ORDER_NUMBER) FROM RECURRING_ORDERS",nativeQuery = true)
    Long maxOrderNumber();

    List<RecurringOrders> findByRestaurantId(User restaurant);

    List<RecurringOrders> findByRestaurantIdAndFromDateLessThanEqualAndToDateGreaterThanEqual(User restaurant, ZonedDateTime fromDate, ZonedDateTime toDate);

    List<RecurringOrders> findByRestaurantIdAndOrderNumber(User restaurant, Long orderNumber);

    List<RecurringOrders> findByOrderNumber(Long orderNumber);

    @Query(value = "SELECT DISTINCT RO.ORDER_NUMBER, CUSTOMER.FIRST_NAME, CUSTOMER.LAST_NAME, CUSTOMER.USER_NAME, LV.LOOKUP_VALUE, LV.LOOKUP_VALUE_CODE " +
            "FROM RECURRING_ORDERS RO " +
            "LEFT OUTER JOIN MENU_ITEM MI ON (MI.MENU_ITEM_ID=RO.MENU_ITEM_ID) " +
            "LEFT OUTER JOIN SUBSCRIPTION S ON (S.SUBSCRIPTION_ID=MI.SUBSCRIPTION_ID) " +
            "LEFT OUTER JOIN USER CUSTOMER ON (CUSTOMER.ID=S.CUSTOMER_ID) " +
            "LEFT OUTER JOIN USER DIETITIAN ON (DIETITIAN.ID=S.DIETITIAN_ID) " +
            "LEFT OUTER JOIN LOOKUP_VALUE LV ON (LV.LOOKUP_VALUE_ID=RO.ORDER_STATUS) " +
            "WHERE DIETITIAN.USER_NAME=?1", nativeQuery = true)
    List<Object[]> getRecurringOrderDetailsForDietitian(String dietitianUserName);
}
