package com.nutri.rest.service;

import com.nutri.rest.mapper.LookupMapper;
import com.nutri.rest.repository.LookupRepository;
import com.nutri.rest.response.ItemDetailsResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LookupService {

    private final LookupRepository lookupRepository;

    public LookupService(LookupRepository lookupRepository) {
        this.lookupRepository = lookupRepository;
    }

    public List<ItemDetailsResponse.LookupUnits> getLookupsByType(Long lookupType){
        return lookupRepository.findByLookupValueType(lookupType).stream().map(LookupMapper::mapToLookups).collect(Collectors.toList());
    }
}
