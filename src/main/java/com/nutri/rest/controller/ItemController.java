package com.nutri.rest.controller;

import com.nutri.rest.request.ItemRequest;
import com.nutri.rest.response.ItemDetailsResponse;
import com.nutri.rest.response.ItemResponse;
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

    @GetMapping("/dietitian/customer/items/{customerName}")
    @ApiOperation(value = "Get items for a subscription")
    public ResponseEntity<Object> getItemsForASubscription(@PathVariable String customerName){
        return new ResponseEntity<>(subscriptionService.getItemsForASubscription(customerName), HttpStatus.OK);
    }

    @PostMapping("/dietitian/customer/items/{customerName}")
    @ApiOperation(value = "Add or update item to a subscription")
    public ItemResponse addOrUpdateItemToASubscription(@PathVariable String customerName, @RequestBody ItemRequest itemRequest){
        return subscriptionService.addOrUpdateItemToSubscription(customerName, itemRequest);
    }

    @DeleteMapping("/dietitian/customer/items/{customerName}/{itemName}")
    @ApiOperation(value = "Delete item in a subscription")
    public ResponseEntity<Object> deleteItemInASubscription(@PathVariable String customerName, @PathVariable String itemName){
        subscriptionService.deleteItemInSubscription(customerName, itemName);
        return new ResponseEntity<>("Item deleted successfully", HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation(value = "Get all item names and supported units")
    public List<ItemDetailsResponse> getItems(){
        return itemService.getAllItems();
    }
}