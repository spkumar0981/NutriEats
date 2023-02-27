package com.nutri.rest.repository;

import com.nutri.rest.model.LookupValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LookupRepository extends JpaRepository<LookupValue, Long> {

    LookupValue findByLookupValueCode(String lookupValueCode);
    List<LookupValue> findByLookupValueType(Long lookupValueType);
}
