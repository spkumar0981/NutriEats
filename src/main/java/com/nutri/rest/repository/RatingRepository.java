package com.nutri.rest.repository;

import com.nutri.rest.model.Rating;
import com.nutri.rest.model.Role;
import com.nutri.rest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByToUserId(User user); //Gets all the ratings of a specific user
    Rating findByFromUserIdAndToUserId(User fromUser, User toUser); //Gets specific rating of user combinations

    @Query(value = "SELECT SUM(RATING)/COUNT(*) FROM RATING WHERE TO_USER_ID=?1",nativeQuery = true)
    Double avgRating(Long dietitianId);
}
