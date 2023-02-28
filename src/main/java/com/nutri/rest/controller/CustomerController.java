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
@RequestMapping("/api/v1/customers")
@Api(value = "Customer Controller")
public class CustomerController {

    private final SubscriptionService subscriptionService;

    public CustomerController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    @ApiOperation(value = "Get all customers")
    public Page<CustomerListResponse> getAllCustomers(Pageable pageable){
        return subscriptionService.getAllCustomers(pageable);
    }

    @GetMapping("/dietitians")
    @ApiOperation(value = "Get all Hired Dietitians of a customer")
    public Page<DietitianListResponse> getAllHiredDietitiansOfACustomer(Pageable pageable){
        return subscriptionService.getAllHiredDietitiansOfACustomer(pageable);
    }

    @PostMapping("/hireDietitian")
    @ApiOperation(value = "Hire a Dietitian")
    public ResponseEntity<Object> hireDietitian(@Valid @RequestBody DietitianRequest dietitianRequest){
        subscriptionService.hireDietitian(dietitianRequest);
        return new ResponseEntity<>("Dietitian hired successfully", HttpStatus.OK);
    }

    @PostMapping("/msgdietitan")
    @ApiOperation(value = "Send Message to customer")
    public ResponseEntity<Object> sendMessageToDietitian(@Valid @RequestBody DietitianRequest dietitianRequest){
        subscriptionService.sendMessageToDietitian(dietitianRequest);
        return new ResponseEntity<>("Message sent successfully", HttpStatus.OK);
    }

    @PostMapping("/confirmmenu")
    @ApiOperation(value = "Confirm a Dietitian's Menu")
    public ResponseEntity<Object> confirmDietitianMenu(@Valid @RequestBody DietitianRequest dietitianRequest){
        subscriptionService.confirmDietitianMenu(dietitianRequest);
        return new ResponseEntity<>("Menu confirmed successfully", HttpStatus.OK);
    }

    @PostMapping("/rejectmenu")
    @ApiOperation(value = "Confirm a Dietitian's Menu")
    public ResponseEntity<Object> rejectDietitianMenu(@Valid @RequestBody DietitianRequest dietitianRequest){
        subscriptionService.rejectDietitianMenu(dietitianRequest);
        return new ResponseEntity<>("Menu rejected successfully", HttpStatus.OK);
    }
}
