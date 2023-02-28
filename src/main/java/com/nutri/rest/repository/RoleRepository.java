package com.nutri.rest.repository;

import com.nutri.rest.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findByCodeValue(String value);
    List<Role> findByCodeName(String name);
}
