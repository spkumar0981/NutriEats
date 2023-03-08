package com.nutri.rest.repository;

import com.nutri.rest.model.DietitianProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DietitianProfileRepository extends JpaRepository<DietitianProfile, Long> {
}
