package com.nutri.rest.controller;

import com.nutri.rest.request.OrderRequest;
import com.nutri.rest.request.RecurringOrderRequest;
import com.nutri.rest.response.OrderResponse;
import com.nutri.rest.response.RecurringOrderDetailsResponse;
import com.nutri.rest.response.RecurringOrderResponse;
import com.nutri.rest.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@Api(value = "Order Controller")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/customer")
    @ApiOperation(value = "Get all customer orders")
    public List<OrderResponse> getAllCustomerOrders(){
        return orderService.getCreatedOrdersForCustomer();
    }

    @GetMapping("/restaurant")
    @ApiOperation(value = "Get all restaurant orders")
    public List<OrderResponse> getAllRestaurantOrders(){
        return orderService.getCreatedOrdersForRestaurant();
    }

    @GetMapping("/customer/{customerUserName}")
    @ApiOperation(value = "Get customer orders created by dietitian")
    public List<OrderResponse> getAllOrdersForCustomer(@PathVariable String customerUserName){
        return orderService.getCreatedOrdersForCustomerByCustomerUsername(customerUserName);
    }

    @GetMapping("/dietitian/customer/new")
    @ApiOperation(value = "Get new recurring orders created by dietitian for customer")
    public List<OrderResponse> getRecurringOrdersForCustomer(@PathVariable String customerUserName){
        return orderService.getCreatedOrdersForCustomerByCustomerUsername(customerUserName);
    }

    @PostMapping("/customer")
    @ApiOperation(value = "Create customer orders")
    public OrderResponse createOrderForCustomer(@RequestParam String restaurantUserName, @RequestBody List<OrderRequest> orderRequestList){
        return orderService.createOrder(orderRequestList, restaurantUserName);
    }

    @PostMapping("/recurring")
    @ApiOperation(value = "Create recurring orders for customer")
    public String createRecurringOrderForCustomerByDietitian(@RequestParam String customerUserName, @RequestBody List<RecurringOrderRequest> orderRequestList){
        return orderService.createRecurringOrder(orderRequestList, customerUserName);
    }

    @PostMapping("/recurring/update")
    @ApiOperation(value = "Update recurring orders from restaurant")
    public ResponseEntity<Object> updateRecurringOrderForCustomerByDietitian(@RequestBody List<RecurringOrderDetailsResponse> orderRequestList){
        orderService.updateNewRecurringOrdersForRestaurantDetails(orderRequestList);
        return new ResponseEntity<>("Completed", HttpStatus.OK);
    }

    @PostMapping("/recurring/restaurant/confirm")
    @ApiOperation(value = "Update recurring orders from restaurant")
    public ResponseEntity<Object> confirmRecurringOrderForCustomerByRestaurant(@RequestParam String orderNumber){
        orderService.confirmNewRecurringOrdersByRestaurant(orderNumber);
        return new ResponseEntity<>("Completed", HttpStatus.OK);
    }

    @GetMapping("/recurring")
    @ApiOperation(value = "Get new recurring restaurant orders")
    public List<RecurringOrderResponse> getNewRecurringOrdersForRestaurant(){
        return orderService.getNewRecurringOrdersForRestaurant();
    }

    @GetMapping("/recurring/{orderId}")
    @ApiOperation(value = "Get details wrt new recurring restaurant orders")
    public List<RecurringOrderDetailsResponse> getNewRecurringOrdersForRestaurantDetails(@PathVariable String orderId){
        return orderService.getNewRecurringOrdersForRestaurantDetails(orderId);
    }

    @GetMapping("/recurring/dietitian")
    @ApiOperation(value = "Get details wrt new recurring restaurant orders")
    public List<RecurringOrderResponse> getNewRecurringOrdersByDietitian(){
        return orderService.getRecurringOrdersCreatedByDietitian();
    }

    @GetMapping("/recurring/dietitian/{orderId}")
    @ApiOperation(value = "Get details wrt new recurring restaurant orders")
    public List<RecurringOrderDetailsResponse> getNewRecurringOrdersByDietitianInDetail(@PathVariable String orderId){
        return orderService.getNewRecurringOrdersForDietitianDetails(orderId);
    }

    @PostMapping("/recurring/update/dietitian")
    @ApiOperation(value = "Create recurring orders for customer")
    public ResponseEntity<Object> updateRecurringOrderForCustomerByDietitian(@RequestParam String orderId){
        orderService.updateNewRecurringOrdersByDietitian(orderId);
        return new ResponseEntity<>("Completed", HttpStatus.OK);
    }
}