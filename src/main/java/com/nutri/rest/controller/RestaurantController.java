package com.nutri.rest.controller;

import com.nutri.rest.request.DietitianRequest;
import com.nutri.rest.response.CustomerListResponse;
import com.nutri.rest.response.DietitianListResponse;
import com.nutri.rest.service.SubscriptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/restaurants")
@Api(value = "Dietitian Controller")
public class RestaurantController {

    private final SubscriptionService subscriptionService;

    public RestaurantController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    @ApiOperation(value = "Get all Restaurant")
    public Page<DietitianListResponse> getAllDietitians(Pageable pageable){
        return subscriptionService.getAllDietitians(pageable);
    }
}
