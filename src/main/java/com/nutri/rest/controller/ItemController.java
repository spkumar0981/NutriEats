package com.nutri.rest.controller;

import com.nutri.rest.request.ItemRequest;
import com.nutri.rest.response.ItemDetailsResponse;
import com.nutri.rest.response.ItemResponse;
import com.nutri.rest.response.RestaurantListResponse;
import com.nutri.rest.service.ItemService;
import com.nutri.rest.service.SubscriptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/items")
@Api(value = "Customer Controller")
public class ItemController {

    private final SubscriptionService subscriptionService;
    private final ItemService itemService;

    public ItemController(SubscriptionService subscriptionService, ItemService itemService) {
        this.subscriptionService = subscriptionService;
        this.itemService = itemService;
    }

    @GetMapping("/dietitian/customer/{customerName}")
    @ApiOperation(value = "Get items for a subscription")
    public ResponseEntity<Object> getItemsForASubscriptionForACustomer(@PathVariable String customerName){
        return new ResponseEntity<>(subscriptionService.getItemsForASubscriptionForCustomer(customerName), HttpStatus.OK);
    }

    @GetMapping("/dietitian/customer/{itemName}/unit/lookupValue")
    @ApiOperation(value = "Get items for a subscription")
    public ResponseEntity<Object> getItemLookups(@PathVariable String itemName){
        return new ResponseEntity<>(subscriptionService.getItemUnits(itemName), HttpStatus.OK);
    }

    @PostMapping("/dietitian/customer/{customerName}")
    @ApiOperation(value = "Add or update item to a subscription")
    public ItemResponse addOrUpdateItemToASubscription(@PathVariable String customerName, @RequestBody ItemRequest itemRequest){
        return subscriptionService.addOrUpdateItemToSubscription(customerName, itemRequest);
    }

    @PostMapping("/dietitian/customer/{customerName}/{itemName}")
    @ApiOperation(value = "Delete item in a subscription")
    public ResponseEntity<Object> deleteItemInASubscription(@PathVariable String customerName, @PathVariable String itemName){
        return new ResponseEntity<>(subscriptionService.deleteItemInSubscription(customerName, itemName), HttpStatus.OK);
    }

    @GetMapping("/customer/dietitian/{dietitianName}")
    @ApiOperation(value = "Get items for a subscription")
    public ResponseEntity<Object> getItemsForASubscriptionForADietitian(@PathVariable String dietitianName){
        return new ResponseEntity<>(subscriptionService.getItemsForASubscriptionForDietitian(dietitianName), HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation(value = "Get all item names and supported units")
    public List<ItemDetailsResponse> getItems(){
        return itemService.getAllItems();
    }

    @GetMapping("/child/items/{parentItemName}")
    @ApiOperation(value = "Get child items")
    public List<ItemDetailsResponse> getChildItems(@PathVariable String parentItemName){
        return itemService.getChildItemsOfParent(parentItemName);
    }

    @GetMapping("/restaurants/{parentItemName}")
    @ApiOperation(value = "Get available restaurants where the corresponding items are served")
    public List<RestaurantListResponse> getRestaurantItems(@PathVariable String parentItemName){
        return itemService.getRestaurantsByParentItem(parentItemName);
    }
}
