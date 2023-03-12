package com.nutri.rest.controller;

import com.nutri.rest.request.DietitianRequest;
import com.nutri.rest.request.common.RestaurantItemsReqAndResp;
import com.nutri.rest.response.CustomerListResponse;
import com.nutri.rest.response.DietitianListResponse;
import com.nutri.rest.response.RestaurantListResponse;
import com.nutri.rest.service.RestaurantService;
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

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    @ApiOperation(value = "Get all Restaurants")
    public Page<RestaurantListResponse> getAllRestaurants(Pageable pageable){
        return restaurantService.getAllRestaurants(pageable);
    }

    @GetMapping("/items")
    @ApiOperation(value = "Get Items served by Restaurant")
    public Page<RestaurantItemsReqAndResp> getRestaurantItems(Pageable pageable){
        return restaurantService.getRestaurantItemsWhenLogged(pageable);
    }



    @GetMapping("/{restaurantUsername}/items")
    @ApiOperation(value = "Get Items served by Restaurant")
    public Page<RestaurantItemsReqAndResp> getRestaurantItems(@PathVariable String restaurantUsername, Pageable pageable){
        return restaurantService.getRestaurantItemsWhenNotLogged(restaurantUsername, pageable);
    }

    @PostMapping("/items")
    @ApiOperation(value = "Create Items served by Restaurant")
    public ResponseEntity<Object> postRestaurantItems(@RequestBody RestaurantItemsReqAndResp itemsReqAndResp, Pageable pageable){
        restaurantService.createRestaurantItems(itemsReqAndResp, pageable);
        return new ResponseEntity<>("Item created successfully", HttpStatus.OK);
    }
}
