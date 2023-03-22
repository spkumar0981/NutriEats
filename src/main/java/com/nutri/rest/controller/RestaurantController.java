package com.nutri.rest.controller;

import com.nutri.rest.request.RestaurantItemsRequest;
import com.nutri.rest.response.RestaurantItemsResponse;
import com.nutri.rest.response.RestaurantListResponse;
import com.nutri.rest.service.RestaurantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

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
    public Collection<RestaurantItemsResponse> getRestaurantItems(){
        return restaurantService.getRestaurantItemsWhenLogged();
    }

    @GetMapping("/{restaurantUsername}/items")
    @ApiOperation(value = "Get Items served by Restaurant")
    public Collection<RestaurantItemsResponse> getRestaurantItems(@PathVariable String restaurantUsername){
        return restaurantService.getRestaurantItemsWhenNotLogged(restaurantUsername);
    }

    @PostMapping("/items")
    @ApiOperation(value = "Create Items served by Restaurant")
    public ResponseEntity<Object> postRestaurantItems(@RequestBody RestaurantItemsRequest itemsRequest){
        restaurantService.createRestaurantItems(itemsRequest);
        return new ResponseEntity<>("Item created successfully", HttpStatus.OK);
    }
}
