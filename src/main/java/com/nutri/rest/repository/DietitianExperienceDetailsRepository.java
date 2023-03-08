package com.nutri.rest.repository;

import com.nutri.rest.model.DietitianExperienceDetails;
import com.nutri.rest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DietitianExperienceDetailsRepository extends JpaRepository<DietitianExperienceDetails, Long> {
    List<DietitianExperienceDetails> findByUserId(User user);
}
