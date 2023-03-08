package com.nutri.rest.repository;

import com.nutri.rest.model.DietitianRecognitions;
import com.nutri.rest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DietitianRecognitionsRepository extends JpaRepository<DietitianRecognitions, Long> {

    List<DietitianRecognitions> findByUserId(User user);
}
