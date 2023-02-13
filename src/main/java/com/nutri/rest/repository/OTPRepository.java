package com.nutri.rest.repository;

import com.nutri.rest.model.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OTPRepository extends JpaRepository<OTP, String> {

    Optional<OTP> findByUserName(String userName);

    @Query(value = "SELECT o FROM OTP o WHERE o.userName = :userName and o.createdDate BETWEEN :startDate AND :endDate")
    List<OTP> findByUserNameAndDateBetween(String userName, LocalDateTime startDate, LocalDateTime endDate);
}
