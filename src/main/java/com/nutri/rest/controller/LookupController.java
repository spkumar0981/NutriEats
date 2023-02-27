package com.nutri.rest.controller;

import com.nutri.rest.request.DietitianRequest;
import com.nutri.rest.response.ItemDetailsResponse;
import com.nutri.rest.service.LookupService;
import com.nutri.rest.service.SubscriptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/lookups")
@Api(value = "Dietitian Controller")
public class LookupController {

    private final LookupService lookupService;

    public LookupController(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    @GetMapping("/{typeId}")
    @ApiOperation(value = "Get lookup values by lookup type")
    public List<ItemDetailsResponse.LookupUnits> getLookupsByTypeId(@PathVariable Long typeId){
        return lookupService.getLookupsByType(typeId);
    }
}
