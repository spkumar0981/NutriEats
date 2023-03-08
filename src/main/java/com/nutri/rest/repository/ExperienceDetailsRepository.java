package com.nutri.rest.repository;

import com.nutri.rest.model.ExperienceDetails;
import com.nutri.rest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperienceDetailsRepository extends JpaRepository<ExperienceDetails, Long> {
    List<ExperienceDetails> findByUserId(User user);
}
