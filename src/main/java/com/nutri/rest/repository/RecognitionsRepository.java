package com.nutri.rest.repository;

import com.nutri.rest.model.Recognitions;
import com.nutri.rest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecognitionsRepository extends JpaRepository<Recognitions, Long> {

    List<Recognitions> findByUserId(User user);
}
