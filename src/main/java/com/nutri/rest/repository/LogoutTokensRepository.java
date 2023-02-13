package com.nutri.rest.repository;

import com.nutri.rest.model.LogoutTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogoutTokensRepository extends JpaRepository<LogoutTokens, Long> {
    LogoutTokens findByToken(String token);
}
