package com.nutri.rest.repository;

import com.nutri.rest.model.LookupValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LookupRepository extends JpaRepository<LookupValue, Long> {

    LookupValue findByLookupValueCode(String lookupValueCode);
}
