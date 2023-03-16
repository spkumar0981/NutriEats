package com.nutri.rest.repository;

import com.nutri.rest.model.User;
import com.nutri.rest.response.CustomerListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String user);

    Optional<User> findByPhoneNumber(String number);

    @Query(value = "SELECT U.* FROM USER U " +
            "INNER JOIN USER_ROLE UR ON (UR.USER_ID=U.ID) " +
            "INNER JOIN ROLE R ON (UR.ROLE_ID=R.ID) " +
            "WHERE R.CODE_NAME = ?1",
            countQuery = "SELECT COUNT(*) FROM USER U " +
                    "INNER JOIN USER_ROLE UR ON (UR.USER_ID=U.ID) " +
                    "INNER JOIN ROLE R ON (UR.ROLE_ID=R.ID) " +
                    "WHERE R.CODE_NAME = ?1",
            nativeQuery = true)
    Page<User> findByUserType(String lastname, Pageable pageable);

    @Query(value = "SELECT CUSTOMER.FIRST_NAME, CUSTOMER.LAST_NAME, CUSTOMER.USER_NAME, CUSTOMER.PHONE_NUMBER, LV.LOOKUP_VALUE_CODE, LV.LOOKUP_VALUE, S.CUSTOMER_INPUT, S.DIETITIAN_INPUT, S.AMOUNT, MEAL.LOOKUP_VALUE_CODE AS MEAL_LOOKUP_CODE, MEAL.LOOKUP_VALUE AS MEAL_LOOKUP_VALUE, S.ALLERGENS " +
            "FROM USER CUSTOMER " +
            "INNER JOIN USER_ROLE UR ON (UR.USER_ID=CUSTOMER.ID) " +
            "INNER JOIN ROLE R ON (UR.ROLE_ID=R.ID) " +
            "INNER JOIN SUBSCRIPTION S ON (S.CUSTOMER_ID=CUSTOMER.ID) " +
            "INNER JOIN LOOKUP_VALUE LV ON (S.STATUS=LV.LOOKUP_VALUE_ID) " +
            "INNER JOIN LOOKUP_VALUE MEAL ON (S.PREFERRED_MEAL_OPTION=MEAL.LOOKUP_VALUE_ID) " +
            "INNER JOIN USER DIETITIAN ON (S.DIETITIAN_ID=DIETITIAN.ID) " +
            "WHERE R.CODE_NAME = 'ROLE_CUSTOMER' AND DIETITIAN.USER_NAME=?1",
            countQuery = "SELECT COUNT(CUSTOMER.*) FROM USER CUSTOMER " +
                    "INNER JOIN USER_ROLE UR ON (UR.USER_ID=CUSTOMER.ID) " +
                    "INNER JOIN ROLE R ON (UR.ROLE_ID=R.ID) " +
                    "INNER JOIN SUBSCRIPTION S ON (S.CUSTOMER_ID=CUSTOMER.ID) " +
                    "INNER JOIN LOOKUP_VALUE LV ON (S.STATUS=LV.LOOKUP_VALUE_ID) " +
                    "INNER JOIN LOOKUP_VALUE MEAL ON (S.PREFERRED_MEAL_OPTION=MEAL.LOOKUP_VALUE_ID) " +
                    "INNER JOIN USER DIETITIAN ON (S.DIETITIAN_ID=DIETITIAN.ID) " +
                    "WHERE R.CODE_NAME = 'ROLE_CUSTOMER' AND DIETITIAN.USER_NAME=?1",
            nativeQuery = true)
    Page<Object[]> getAllCustomersForADietitian(String dietitianUsername, Pageable pageable);

        @Query(value = "SELECT CUSTOMER.FIRST_NAME, CUSTOMER.LAST_NAME, CUSTOMER.USER_NAME, CUSTOMER.PHONE_NUMBER, LV.LOOKUP_VALUE_CODE, LV.LOOKUP_VALUE, S.CUSTOMER_INPUT, S.DIETITIAN_INPUT, S.AMOUNT, MEAL.LOOKUP_VALUE_CODE AS MEAL_LOOKUP_CODE, MEAL.LOOKUP_VALUE AS MEAL_LOOKUP_VALUE, S.ALLERGENS " +
                "FROM USER CUSTOMER " +
                "INNER JOIN USER_ROLE UR ON (UR.USER_ID=CUSTOMER.ID) " +
                "INNER JOIN ROLE R ON (UR.ROLE_ID=R.ID) " +
                "INNER JOIN SUBSCRIPTION S ON (S.CUSTOMER_ID=CUSTOMER.ID) " +
                "INNER JOIN LOOKUP_VALUE LV ON (S.STATUS=LV.LOOKUP_VALUE_ID) " +
                "INNER JOIN LOOKUP_VALUE MEAL ON (S.PREFERRED_MEAL_OPTION=MEAL.LOOKUP_VALUE_ID) " +
                "INNER JOIN USER DIETITIAN ON (S.DIETITIAN_ID=DIETITIAN.ID) " +
                "WHERE R.CODE_NAME = 'ROLE_CUSTOMER' AND DIETITIAN.USER_NAME=?1 AND (LV.LOOKUP_VALUE_CODE=?2 OR LV.LOOKUP_VALUE_CODE=?3 OR LV.LOOKUP_VALUE_CODE=?4)",
            countQuery = "SELECT COUNT(CUSTOMER.*) FROM USER CUSTOMER " +
                    "INNER JOIN USER_ROLE UR ON (UR.USER_ID=CUSTOMER.ID) " +
                    "INNER JOIN ROLE R ON (UR.ROLE_ID=R.ID) " +
                    "INNER JOIN SUBSCRIPTION S ON (S.CUSTOMER_ID=CUSTOMER.ID) " +
                    "INNER JOIN LOOKUP_VALUE LV ON (S.STATUS=LV.LOOKUP_VALUE_ID) " +
                    "INNER JOIN LOOKUP_VALUE MEAL ON (S.PREFERRED_MEAL_OPTION=MEAL.LOOKUP_VALUE_ID) " +
                    "INNER JOIN USER DIETITIAN ON (S.DIETITIAN_ID=DIETITIAN.ID) " +
                    "WHERE R.CODE_NAME = 'ROLE_CUSTOMER' AND DIETITIAN.USER_NAME=?1 AND (LV.LOOKUP_VALUE_CODE=?2 OR LV.LOOKUP_VALUE_CODE=?3 OR LV.LOOKUP_VALUE_CODE=?4)",
            nativeQuery = true)
    Page<Object[]> getAllNewCustomersForADietitian(String dietitianUsername, String subscriptionStatus1, String subscriptionStatus2, String subscriptionStatus3, Pageable pageable);

    @Query(value = "SELECT DIETITIAN.FIRST_NAME, DIETITIAN.LAST_NAME, DIETITIAN.USER_NAME, DIETITIAN.PHONE_NUMBER, LV.LOOKUP_VALUE_CODE, LV.LOOKUP_VALUE, S.CUSTOMER_INPUT, S.DIETITIAN_INPUT, S.AMOUNT, MEAL.LOOKUP_VALUE_CODE AS MEAL_LOOKUP_CODE, MEAL.LOOKUP_VALUE AS MEAL_LOOKUP_VALUE, S.ALLERGENS " +
            "FROM USER DIETITIAN " +
            "INNER JOIN USER_ROLE UR ON (UR.USER_ID=DIETITIAN.ID) " +
            "INNER JOIN ROLE R ON (UR.ROLE_ID=R.ID) " +
            "INNER JOIN SUBSCRIPTION S ON (S.DIETITIAN_ID=DIETITIAN.ID) " +
            "INNER JOIN LOOKUP_VALUE LV ON (S.STATUS=LV.LOOKUP_VALUE_ID) " +
            "INNER JOIN LOOKUP_VALUE MEAL ON (S.PREFERRED_MEAL_OPTION=MEAL.LOOKUP_VALUE_ID) " +
            "INNER JOIN USER CUSTOMER ON (S.CUSTOMER_ID=CUSTOMER.ID) " +
            "WHERE R.CODE_NAME = 'ROLE_DIETITIAN' AND CUSTOMER.USER_NAME=?1",
            countQuery = "SELECT COUNT(DIETITIAN.*) " +
                    "FROM USER DIETITIAN " +
                    "INNER JOIN USER_ROLE UR ON (UR.USER_ID=DIETITIAN.ID) " +
                    "INNER JOIN ROLE R ON (UR.ROLE_ID=R.ID) " +
                    "INNER JOIN SUBSCRIPTION S ON (S.DIETITIAN_ID=DIETITIAN.ID) " +
                    "INNER JOIN LOOKUP_VALUE LV ON (S.STATUS=LV.LOOKUP_VALUE_ID) " +
                    "INNER JOIN LOOKUP_VALUE MEAL ON (S.PREFERRED_MEAL_OPTION=MEAL.LOOKUP_VALUE_ID) " +
                    "INNER JOIN USER CUSTOMER ON (S.CUSTOMER_ID=CUSTOMER.ID) " +
                    "WHERE R.CODE_NAME = 'ROLE_DIETITIAN' AND CUSTOMER.USER_NAME=?1",
            nativeQuery = true)
    Page<Object[]> getAllHiredDietitiansOfCustomer(String customerUsername, Pageable pageable);
}
