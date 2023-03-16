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
@RequestMapping("/api/v1/dietitians")
@Api(value = "Dietitian Controller")
public class DietitianController {

    private final SubscriptionService subscriptionService;

    public DietitianController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    @ApiOperation(value = "Get all Dietitians")
    public Page<DietitianListResponse> getAllDietitians(Pageable pageable){
        return subscriptionService.getAllDietitians(pageable);
    }

    @PostMapping("/msgcustomer")
    @ApiOperation(value = "Send Message to customer")
    public ResponseEntity<Object> sendMessageToCustomer(@Valid @RequestBody DietitianRequest dietitianRequest){
        subscriptionService.sendMessageToCustomer(dietitianRequest);
        return new ResponseEntity<>("Message sent successfully", HttpStatus.OK);
    }

    @PostMapping("/customer/items")
    @ApiOperation(value = "Confirm meal for a subscription")
    public ResponseEntity<Object> confirmMealForASubscription(@Valid @RequestBody DietitianRequest customer){
        subscriptionService.confirmMealForASubscription(customer);
        return new ResponseEntity<>("Meal Confirmed", HttpStatus.OK);
    }

    @GetMapping("/customers")
    @ApiOperation(value = "Get all customers of a dietitian")
    public Page<CustomerListResponse> getAllCustomersOfADietitian(Pageable pageable){
        return subscriptionService.getAllCustomersForADietitian(pageable);
    }

    @GetMapping("/new/customers")
    @ApiOperation(value = "Get customers of a dietitian for whom dietitian has to reply")
    public Page<CustomerListResponse> getAllNewCustomersForADietitian(Pageable pageable){
        return subscriptionService.getAllNewCustomersForADietitian(pageable);
    }
}
