package com.nutri.rest.service;

import com.nutri.rest.model.*;
import com.nutri.rest.repository.*;
import com.nutri.rest.request.OrderRequest;
import com.nutri.rest.request.RecurringOrderRequest;
import com.nutri.rest.response.*;
import com.nutri.rest.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.nutri.rest.utils.LookupTypes.ORDER_STATUS_TYPE;
import static com.nutri.rest.utils.OrderStatus.*;

@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final UserRepository userRepository;
    private final ChildItemRepository childItemRepository;
    private final RestaurantItemsRepository restaurantItemsRepository;
    private final RestaurantItemWeightsAndPricesRepository itemWeightsAndPricesRepository;
    private final LookupRepository lookupRepository;
    private final MenuItemRepository menuItemRepository;
    private final ParentItemRepository parentItemRepository;
    private final RecurringOrderRepository recurringOrderRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final MailService mailService;

    public OrderService(OrderRepository orderRepository, OrderItemsRepository orderItemsRepository, UserRepository userRepository, ChildItemRepository childItemRepository, RestaurantItemsRepository restaurantItemsRepository, RestaurantItemWeightsAndPricesRepository itemWeightsAndPricesRepository, LookupRepository lookupRepository, MenuItemRepository menuItemRepository, ParentItemRepository parentItemRepository, RecurringOrderRepository recurringOrderRepository, SubscriptionRepository subscriptionRepository, MailService mailService) {
        this.orderRepository = orderRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.userRepository = userRepository;
        this.childItemRepository = childItemRepository;
        this.restaurantItemsRepository = restaurantItemsRepository;
        this.itemWeightsAndPricesRepository = itemWeightsAndPricesRepository;
        this.lookupRepository = lookupRepository;
        this.menuItemRepository = menuItemRepository;
        this.parentItemRepository = parentItemRepository;
        this.recurringOrderRepository = recurringOrderRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.mailService = mailService;
    }

    public OrderResponse createOrder(List<OrderRequest> orderRequestList, String restaurantUserName, String deliveryAddress){
        User restaurant = userRepository.findByUserName(restaurantUserName).get();
        User customer = getCurrentLoggedUserDetails();

        Order order = Order.builder()
                .orderStatusId(lookupRepository.findByLookupValueCode(OrderStatus.ORDER_STATUS_1.name()))
                .customerId(customer)
                .restaurantId(restaurant)
                .deliveryAddress(deliveryAddress)
                .orderDate(LocalDateTime.now())
                .build();
        Order finalOrder = orderRepository.save(order);

        List<OrderItems> orderItemsList = orderRequestList.stream().map(request -> {
            ChildItem childItem = childItemRepository.findByItemName(request.getChildItemName());
            RestaurantItems restaurantItem = restaurantItemsRepository.findByRestaurantIdAndChildItemId(restaurant, childItem);
            LookupValue lookupValue = lookupRepository.findByLookupValueCode(request.getItemWeightsAndPrices().getQuantityUnit().getUnitLookupCode());
            RestaurantItemWeightsAndPrices itemWeightsAndPrices = itemWeightsAndPricesRepository
                    .findByRestaurantItemIdAndQuantityAndQuantityUnit(restaurantItem, request.getItemWeightsAndPrices().getQuantity(),
                            lookupValue);
            Optional<RecurringOrders> recurringOrders = recurringOrderRepository.findById(request.getRecurringOrderId() !=null ? Long.parseLong(request.getRecurringOrderId().replace("ORDER-","")) : -1);
            return OrderItems.builder()
                    .orderId(finalOrder)
                    .childItem(childItem)
                    .quantity(request.getQuantity())
                    .itemWeightsAndPrices(itemWeightsAndPrices)
                    .recurringOrderId(recurringOrders !=null ? recurringOrders.isPresent() ? recurringOrders.get() : null : null)
                    .build();
        }).collect(Collectors.toList());

        List<OrderItemResponse> orderResponseList = orderItemsRepository.saveAll(orderItemsList).stream().map(orderItem ->
                OrderItemResponse.builder().build()).collect(Collectors.toList());
        return OrderResponse.builder()
                .orderId("ORDER-"+order.getOrderId())
                .orderItems(orderResponseList)
                .orderStatus(order.getOrderStatusId().getLookupValue())
                .customerName(customer.getFirstName()+", "+customer.getLastName())
                .restaurantName(order.getRestaurantId().getRestaurantProfile().getRestaurantName())
                .dietitianName(order.getDietitianId()!=null  ? order.getDietitianId().getFirstName()+", "+order.getDietitianId().getLastName() : null)
                .orderTotalPrice(order.getOrderTotalPrice())
                .build();
    }

    public OrderResponse createOrderForCustomerByRestaurant(List<OrderRequest> orderRequestList){
        RecurringOrders recOrder = recurringOrderRepository.findById(
                Long.parseLong(orderRequestList.get(0).getRecurringOrderId().replace("ORDER-",""))).get();
        User restaurant = recOrder.getRestaurantId();
        User customer = recOrder.getMenuItemId().getSubscriptionId().getCustomerId();
        User dietitian = recOrder.getMenuItemId().getSubscriptionId().getDietitianId();

        Order order = Order.builder()
                .orderStatusId(lookupRepository.findByLookupValueCode(OrderStatus.ORDER_STATUS_1.name()))
                .customerId(customer)
                .restaurantId(restaurant)
                .dietitianId(dietitian)
                .deliveryAddress(recOrder.getDeliveryAddress())
                .orderDate(LocalDateTime.now())
                .build();
        Order finalOrder = orderRepository.save(order);

        List<OrderItems> orderItemsList = orderRequestList.stream().map(request -> {
            ChildItem childItem = childItemRepository.findByItemName(request.getChildItemName());

            Optional<RecurringOrders> recurringOrders = recurringOrderRepository.findById(Long.parseLong(request.getRecurringOrderId().replace("ORDER-","")));
            return OrderItems.builder()
                    .orderId(finalOrder)
                    .childItem(childItem)
                    .quantity(request.getQuantity())
                    .recurringOrderId(recurringOrders.isPresent() ? recurringOrders.get() : null)
                    .build();
        }).collect(Collectors.toList());

        List<OrderItemResponse> orderResponseList = orderItemsRepository.saveAll(orderItemsList).stream().map(orderItem ->
                OrderItemResponse.builder().build()).collect(Collectors.toList());
        return OrderResponse.builder()
                .orderId("ORDER-"+order.getOrderId())
                .orderItems(orderResponseList)
                .orderStatus(order.getOrderStatusId().getLookupValue())
                .customerName(customer.getFirstName()+", "+customer.getLastName())
                .restaurantName(order.getRestaurantId().getRestaurantProfile().getRestaurantName())
                .dietitianName(order.getDietitianId()!=null  ? order.getDietitianId().getFirstName()+", "+order.getDietitianId().getLastName() : null)
                .orderTotalPrice(order.getOrderTotalPrice())
                .build();
    }

    public List<OrderResponse> getNewlyCreatedOrdersForCustomer(){
        User customer = getCurrentLoggedUserDetails();
        LookupValue orderStatus = lookupRepository.findByLookupValueCode(ORDER_STATUS_6.name());
        List<Order> orders = orderRepository.findByCustomerIdAndOrderStatusIdNot(customer, orderStatus);
        return orders.stream().map(order ->
                OrderResponse.builder()
                        .orderId("ORDER-"+order.getOrderId())
                        .orderItems(orderItemsRepository.findByOrderId(order)
                                .stream().map(orderItem -> OrderItemResponse.builder()
                                        .childItemName(orderItem.getChildItem().getItemName())
                                        .parentItemName(orderItem.getChildItem().getParentItem().getItemName())
                                        .quantity(orderItem.getRecurringOrderId()==null ? orderItem.getQuantity() : 1)
                                        .itemWeightsAndPrices(ItemWeightsAndPrices.builder()
                                                .quantity(orderItem.getRecurringOrderId()==null ? orderItem.getItemWeightsAndPrices().getQuantity() :
                                                        orderItem.getRecurringOrderId().getMenuItemId().getQuantity())
                                                .itemPrice(orderItem.getRecurringOrderId()==null ? orderItem.getItemWeightsAndPrices().getItemPrice() : null)
                                                .quantityUnit(ItemDetailsResponse.LookupUnits.builder()
                                                        .unitLookupValue(orderItem.getRecurringOrderId()==null ? orderItem.getItemWeightsAndPrices().getQuantityUnit().getLookupValue()
                                                                : orderItem.getRecurringOrderId().getMenuItemId().getQuantityUnit().getLookupValue())
                                                        .unitLookupCode(orderItem.getRecurringOrderId()==null ? orderItem.getItemWeightsAndPrices().getQuantityUnit().getLookupValueCode()
                                                                : orderItem.getRecurringOrderId().getMenuItemId().getQuantityUnit().getLookupValueCode())
                                                        .build())
                                                .build())
                                        .build()).collect(Collectors.toList()))
                        .orderStatus(order.getOrderStatusId().getLookupValue())
                        .customerName(order.getCustomerId().getFirstName() + ", " + order.getCustomerId().getLastName())
                        .restaurantName(order.getRestaurantId().getRestaurantProfile().getRestaurantName())
                        .dietitianName(order.getDietitianId() != null ? order.getDietitianId().getFirstName() + ", " + order.getDietitianId().getLastName() : null)
                        .orderTotalPrice(order.getOrderTotalPrice())
                        .deliveryAddress(order.getDeliveryAddress())
                        .build()
        ).collect(Collectors.toList());
    }

    public List<OrderResponse> getCreatedOrdersForCustomer(){
        User customer = getCurrentLoggedUserDetails();
        List<Order> orders = orderRepository.findByCustomerId(customer);
        return orders.stream().map(order ->
                OrderResponse.builder()
                        .orderId("ORDER-"+order.getOrderId())
                        .orderItems(orderItemsRepository.findByOrderId(order)
                                .stream().map(orderItem -> OrderItemResponse.builder()
                                        .childItemName(orderItem.getChildItem().getItemName())
                                        .parentItemName(orderItem.getChildItem().getParentItem().getItemName())
                                        .quantity(orderItem.getRecurringOrderId()==null ? orderItem.getQuantity() : 1)
                                        .itemWeightsAndPrices(ItemWeightsAndPrices.builder()
                                                .quantity(orderItem.getRecurringOrderId()==null ? orderItem.getItemWeightsAndPrices().getQuantity() :
                                                        orderItem.getRecurringOrderId().getMenuItemId().getQuantity())
                                                .itemPrice(orderItem.getRecurringOrderId()==null ? orderItem.getItemWeightsAndPrices().getItemPrice() : null)
                                                .quantityUnit(ItemDetailsResponse.LookupUnits.builder()
                                                        .unitLookupValue(orderItem.getRecurringOrderId()==null ? orderItem.getItemWeightsAndPrices().getQuantityUnit().getLookupValue()
                                                                : orderItem.getRecurringOrderId().getMenuItemId().getQuantityUnit().getLookupValue())
                                                        .unitLookupCode(orderItem.getRecurringOrderId()==null ? orderItem.getItemWeightsAndPrices().getQuantityUnit().getLookupValueCode()
                                                                : orderItem.getRecurringOrderId().getMenuItemId().getQuantityUnit().getLookupValueCode())
                                                        .build())
                                                .build())
                                        .build()).collect(Collectors.toList()))
                        .orderStatus(order.getOrderStatusId().getLookupValue())
                        .customerName(order.getCustomerId().getFirstName() + ", " + order.getCustomerId().getLastName())
                        .restaurantName(order.getRestaurantId().getRestaurantProfile().getRestaurantName())
                        .dietitianName(order.getDietitianId() != null ? order.getDietitianId().getFirstName() + ", " + order.getDietitianId().getLastName() : null)
                        .orderTotalPrice(order.getOrderTotalPrice())
                        .deliveryAddress(order.getDeliveryAddress())
                        .build()
        ).collect(Collectors.toList());
    }

    public List<OrderResponse> getCreatedOrdersForCustomerByCustomerUsername(String customerUserName){
        User dietitian = getCurrentLoggedUserDetails();
        User customer = userRepository.findByUserName(customerUserName).get();
        List<Order> orders = orderRepository.findByCustomerIdAndDietitianId(customer, dietitian);
        return orders.stream().map(order ->
                OrderResponse.builder()
                        .orderId("ORDER-"+order.getOrderId())
                        .orderItems(orderItemsRepository.findByOrderId(order)
                                .stream().map(orderItem -> OrderItemResponse.builder()
                                        .childItemName(orderItem.getChildItem().getItemName())
                                        .parentItemName(orderItem.getChildItem().getParentItem().getItemName())
                                        .quantity(orderItem.getQuantity())
                                        .itemWeightsAndPrices(ItemWeightsAndPrices.builder()
                                                .quantity(orderItem.getItemWeightsAndPrices().getQuantity())
                                                .itemPrice(orderItem.getItemWeightsAndPrices().getItemPrice())
                                                .quantityUnit(ItemDetailsResponse.LookupUnits.builder()
                                                        .unitLookupValue(orderItem.getItemWeightsAndPrices().getQuantityUnit().getLookupValue())
                                                        .unitLookupCode(orderItem.getItemWeightsAndPrices().getQuantityUnit().getLookupValueCode())
                                                        .build())
                                                .build())
                                        .build()).collect(Collectors.toList()))
                        .orderStatus(order.getOrderStatusId().getLookupValue())
                        .customerName(customer.getFirstName()+", "+customer.getLastName())
                        .restaurantName(order.getRestaurantId().getRestaurantProfile().getRestaurantName())
                        .dietitianName(order.getDietitianId()!=null  ? order.getDietitianId().getFirstName()+", "+order.getDietitianId().getLastName() : null)
                        .orderTotalPrice(order.getOrderTotalPrice())
                        .deliveryAddress(order.getDeliveryAddress())
                        .build()
        ).collect(Collectors.toList());
    }

    public List<OrderResponse> getCreatedOrdersForRestaurant(){
        User restaurant = getCurrentLoggedUserDetails();
        List<Order> orders = orderRepository.findByRestaurantId(restaurant);
        return orders.stream().map(order -> OrderResponse.builder()
                .orderId("ORDER-" + order.getOrderId())
                .orderItems(orderItemsRepository.findByOrderId(order)
                        .stream().map(orderItem -> OrderItemResponse.builder()
                                .childItemName(orderItem.getChildItem().getItemName())
                                .parentItemName(orderItem.getChildItem().getParentItem().getItemName())
                                .quantity(orderItem.getQuantity())
                                .itemWeightsAndPrices(ItemWeightsAndPrices.builder()
                                        .quantity(orderItem.getItemWeightsAndPrices().getQuantity())
                                        .itemPrice(orderItem.getItemWeightsAndPrices().getItemPrice())
                                        .quantityUnit(ItemDetailsResponse.LookupUnits.builder()
                                                .unitLookupValue(orderItem.getItemWeightsAndPrices().getQuantityUnit().getLookupValue())
                                                .unitLookupCode(orderItem.getItemWeightsAndPrices().getQuantityUnit().getLookupValueCode())
                                                .build())
                                        .build())
                                .build()).collect(Collectors.toList()))
                .orderStatus(order.getOrderStatusId().getLookupValue())
                .customerName(order.getCustomerId().getFirstName() + ", " + order.getCustomerId().getLastName())
                .restaurantName(order.getRestaurantId().getRestaurantProfile().getRestaurantName())
                .dietitianName(order.getDietitianId() != null ? order.getDietitianId().getFirstName() + ", " + order.getDietitianId().getLastName() : null)
                .orderTotalPrice(order.getOrderTotalPrice())
                .deliveryAddress(order.getDeliveryAddress())
                .build()
        ).collect(Collectors.toList());
    }

    public List<OrderResponse> getLiveCreatedOrdersForRestaurant(){
        User restaurant = getCurrentLoggedUserDetails();
        List<Order> orders = orderRepository.findByRestaurantIdAndOrderStatusIdNot(restaurant, lookupRepository.findByLookupValueCode(OrderStatus.ORDER_STATUS_6.name()));

        return orders.stream().map(order -> OrderResponse.builder()
                .orderId("ORDER-" + order.getOrderId())
                .orderItems(orderItemsRepository.findByOrderId(order)
                        .stream().map(orderItem -> OrderItemResponse.builder()
                                .childItemName(orderItem.getChildItem().getItemName())
                                .parentItemName(orderItem.getChildItem().getParentItem().getItemName())
                                .quantity(orderItem.getRecurringOrderId()==null ? orderItem.getQuantity() : 1)
                                .itemWeightsAndPrices(ItemWeightsAndPrices.builder()
                                        .quantity(orderItem.getRecurringOrderId()==null ? orderItem.getItemWeightsAndPrices().getQuantity() :
                                                orderItem.getRecurringOrderId().getMenuItemId().getQuantity())
                                        .itemPrice(orderItem.getRecurringOrderId()==null ? orderItem.getItemWeightsAndPrices().getItemPrice() : null)
                                        .quantityUnit(ItemDetailsResponse.LookupUnits.builder()
                                                .unitLookupValue(orderItem.getRecurringOrderId()==null ? orderItem.getItemWeightsAndPrices().getQuantityUnit().getLookupValue()
                                                        : orderItem.getRecurringOrderId().getMenuItemId().getQuantityUnit().getLookupValue())
                                                .unitLookupCode(orderItem.getRecurringOrderId()==null ? orderItem.getItemWeightsAndPrices().getQuantityUnit().getLookupValueCode()
                                                : orderItem.getRecurringOrderId().getMenuItemId().getQuantityUnit().getLookupValueCode())
                                                .build())
                                        .build())
                                .build()).collect(Collectors.toList()))
                .orderStatus(order.getOrderStatusId().getLookupValue())
                .customerName(order.getCustomerId().getFirstName() + ", " + order.getCustomerId().getLastName())
                .restaurantName(order.getRestaurantId().getRestaurantProfile().getRestaurantName())
                .dietitianName(order.getDietitianId() != null ? order.getDietitianId().getFirstName() + ", " + order.getDietitianId().getLastName() : null)
                .orderTotalPrice(order.getOrderTotalPrice())
                .deliveryAddress(order.getDeliveryAddress())
                .build()
        ).collect(Collectors.toList());
    }

    public ItemDetailsResponse.LookupUnits getNextOrderStatus(String orderId){
        Long orderIdVal = Long.parseLong(orderId.replace("ORDER-",""));
        Order order = orderRepository.findById(orderIdVal).get();
        List<LookupValue> lookupValues = lookupRepository.findByLookupValueType(ORDER_STATUS_TYPE.getValue());
        LookupValue currentLookupValue = order.getOrderStatusId();

        LookupValue resultLookup = null;
        for (LookupValue lookupValue:lookupValues) {
            if(currentLookupValue.getLookupValueId()<lookupValue.getLookupValueId()
                    && ((resultLookup == null) || (resultLookup!=null && resultLookup.getLookupValueId()>lookupValue.getLookupValueId()))){
                resultLookup = lookupValue;
            }
        }
        return ItemDetailsResponse.LookupUnits.builder()
                .unitLookupCode(resultLookup.getLookupValueCode())
                .unitLookupValue(resultLookup.getLookupValue()).build();
    }

    public void setOrderStatus(String orderId){
        Long orderIdVal = Long.parseLong(orderId.replace("ORDER-",""));
        Order order = orderRepository.findById(orderIdVal).get();
        List<LookupValue> lookupValues = lookupRepository.findByLookupValueType(ORDER_STATUS_TYPE.getValue());
        LookupValue currentLookupValue = order.getOrderStatusId();

        LookupValue resultLookup = null;
        for (LookupValue lookupValue:lookupValues) {
            if(currentLookupValue.getLookupValueId()<lookupValue.getLookupValueId()
                    && ((resultLookup == null) || (resultLookup!=null && resultLookup.getLookupValueId()>lookupValue.getLookupValueId()))){
                resultLookup = lookupValue;
            }
        }
        order.setOrderStatusId(resultLookup);
        orderRepository.save(order);
        if(ORDER_STATUS_2.name().equals(resultLookup.getLookupValueCode()))
            mailService.sendOrderConfirmationMailToCustomer(order.getCustomerId().getUserName(), order.getCustomerId().getFirstName(), order.getRestaurantId().getRestaurantProfile().getRestaurantName(), orderIdVal.toString());
        if(ORDER_STATUS_6.name().equals(resultLookup.getLookupValueCode()))
            mailService.sendOrderDeliveredMailToCustomer(order.getCustomerId().getUserName(), order.getCustomerId().getFirstName(), order.getRestaurantId().getRestaurantProfile().getRestaurantName(), orderIdVal.toString());
    }

    public void updateCreatedOrdersForRestaurant(OrderResponse orderRequest){
        long orderIdVal = Long.parseLong(orderRequest.getOrderId().replace("ORDER-",""));
        Order order = orderRepository.findById(orderIdVal).get();
        order.setOrderStatusId(lookupRepository.findByLookupValueCode(orderRequest.getOrderStatusCode()));
        orderRepository.save(order);
    }

    private User getCurrentLoggedUserDetails(){
        String loggedUserName = CurrentUserService.getLoggedUserName();
        return userRepository.findByUserName(loggedUserName).get();
    }

    ///****************************************Recurring Orders***********************************************
    public String createRecurringOrder(List<RecurringOrderRequest> orderRequestList, String customerUserName, String deliveryAddress){
        User customer = userRepository.findByUserName(customerUserName).get();
        User dietitian = getCurrentLoggedUserDetails();

        Long maxOrderNumber = recurringOrderRepository.maxOrderNumber();
        if(maxOrderNumber == null)
            maxOrderNumber = 1L;
        else
            maxOrderNumber = maxOrderNumber + 1;

        Long finalMaxOrderNumber = maxOrderNumber;
        orderRequestList.forEach(recurringOrderRequest -> {
            User restaurant = userRepository.findByUserName(recurringOrderRequest.getRestaurant()).get();
            ParentItem parentItem = parentItemRepository.findByItemName(recurringOrderRequest.getItemName());
            MenuItem menuItem = menuItemRepository.getMenuItem(customer.getId(), dietitian.getId(), parentItem.getItemId());
            RecurringOrders recurringOrders = RecurringOrders.builder()
                    .restaurantId(restaurant)
                    .orderNumber(finalMaxOrderNumber)
                    .orderDeliveryTime(recurringOrderRequest.getDeliveryTime())
                    .fromDate(recurringOrderRequest.getFromDate())
                    .toDate(recurringOrderRequest.getToDate())
                    .menuItemId(menuItem)
                    .orderStatus(lookupRepository.findByLookupValueCode(RecurringOrderStatus.REC_ORDER_STATUS_1.name()))
                    .deliveryAddress(deliveryAddress)
                    .build();
            recurringOrderRepository.save(recurringOrders);
        });

        Subscription subscription = subscriptionRepository.findByCustomerIdAndDietitianId(customer, dietitian);
        subscription.setStatus(lookupRepository.findByLookupValueCode(SubscriptionStatus.SUBSCRIPTION_STATUS_5.name()));

        subscriptionRepository.save(subscription);
        return "REC-ORDER-"+maxOrderNumber;
    }

    public List<RecurringOrderResponse> getNewRecurringOrdersForRestaurant(){
        User restaurant = getCurrentLoggedUserDetails();
        List<RecurringOrders> recurringOrders = recurringOrderRepository.findByRestaurantId(restaurant);
        Map<String, Integer> duplicates = new HashMap<>();
        List<RecurringOrderResponse> recurringOrderResponses = new ArrayList<>();

        recurringOrders.forEach(recurringOrder -> {
            String key = recurringOrder.getOrderNumber()+""+recurringOrder.getRestaurantId().getId()+""
                    +recurringOrder.getMenuItemId().getSubscriptionId().getCustomerId().getId()+""
                    +recurringOrder.getMenuItemId().getSubscriptionId().getDietitianId().getId();

            if(!duplicates.containsKey(key)) {
                recurringOrderResponses.add(RecurringOrderResponse.builder()
                        .orderId("REC-ORDER-" + recurringOrder.getOrderNumber())
                        .restaurantName(recurringOrder.getRestaurantId().getRestaurantProfile().getRestaurantName())
                        .customerName(recurringOrder.getMenuItemId().getSubscriptionId().getCustomerId().getFirstName()+", "
                                +recurringOrder.getMenuItemId().getSubscriptionId().getCustomerId().getLastName())
                        .customerAddress(recurringOrder.getDeliveryAddress())
                        .dietitianName(recurringOrder.getMenuItemId().getSubscriptionId().getDietitianId().getFirstName()+", "
                                +recurringOrder.getMenuItemId().getSubscriptionId().getDietitianId().getLastName())
                        .orderStatus(lookupRepository.findByLookupValueCode(recurringOrder.getOrderStatus().getLookupValueCode()).getLookupValue())
                        .orderStatusCode(recurringOrder.getOrderStatus().getLookupValueCode())
                        .build());
                duplicates.put(key, 1);
            }
        });
        return recurringOrderResponses;
    }

    public List<RecurringOrderDetailsResponse> getNewRecurringOrdersForRestaurantDetails(String orderNumber){
        long orderNumb = Integer.parseInt(orderNumber.replace("REC-ORDER-",""));
        User restaurant = getCurrentLoggedUserDetails();

        List<RecurringOrders> recurringOrders = recurringOrderRepository.findByRestaurantIdAndOrderNumber(restaurant, orderNumb);
        List<RecurringOrderDetailsResponse> recurringOrderResponses = new ArrayList<>();

        recurringOrders.forEach(recurringOrder -> {
            recurringOrderResponses.add(RecurringOrderDetailsResponse.builder()
                    .orderId("ORDER-" + recurringOrder.getOrderId())
                    .itemName(recurringOrder.getMenuItemId().getParentItemId().getItemName())
                    .childItems(recurringOrder.getMenuItemId().getChildItems())
                    .quantityAndUnit(recurringOrder.getMenuItemId().getQuantity()+" "+
                            recurringOrder.getMenuItemId().getQuantityUnit().getLookupValue())
                    .instructions(recurringOrder.getMenuItemId().getInstructions())
                    .fromDate(recurringOrder.getFromDate())
                    .toDate(recurringOrder.getToDate())
                    .deliveryTime(recurringOrder.getOrderDeliveryTime())
                    .price(recurringOrder.getOrderTotalPrice())
                    .build());
        });
        return recurringOrderResponses;
    }

    public List<RecurringOrderResponse> getPendingRecurringOrdersForTheDay(){
        User restaurant = getCurrentLoggedUserDetails();
        List<RecurringOrders> recurringOrders = recurringOrderRepository.findByRestaurantIdAndFromDateLessThanEqualAndToDateGreaterThanEqual(restaurant, LocalDate.now(ZoneId.systemDefault()).atStartOfDay(ZoneId.systemDefault()), LocalDate.now(ZoneId.systemDefault()).plusDays(1).atStartOfDay(ZoneId.systemDefault()));
        Map<String, Integer> duplicates = new HashMap<>();
        List<RecurringOrderResponse> recurringOrderResponses = new ArrayList<>();

        recurringOrders.forEach(recurringOrder -> {
            Long order = orderRepository.findIfOrderCreatedForTheDay(restaurant.getId(), recurringOrder.getOrderId(),
                    LocalDate.now().atStartOfDay(), LocalDateTime.now().with(LocalTime.MAX));
            if(order==null || order==0) {
                String key = recurringOrder.getOrderNumber() + "" + recurringOrder.getRestaurantId().getId() + ""
                        + recurringOrder.getMenuItemId().getSubscriptionId().getCustomerId().getId() + ""
                        + recurringOrder.getMenuItemId().getSubscriptionId().getDietitianId().getId();

                if (!duplicates.containsKey(key)) {
                    recurringOrderResponses.add(RecurringOrderResponse.builder()
                            .orderId("REC-ORDER-" + recurringOrder.getOrderNumber())
                            .restaurantName(recurringOrder.getRestaurantId().getRestaurantProfile().getRestaurantName())
                            .customerName(recurringOrder.getMenuItemId().getSubscriptionId().getCustomerId().getFirstName() + ", "
                                    + recurringOrder.getMenuItemId().getSubscriptionId().getCustomerId().getLastName())
                            .customerAddress(recurringOrder.getDeliveryAddress())
                            .dietitianName(recurringOrder.getMenuItemId().getSubscriptionId().getDietitianId().getFirstName() + ", "
                                    + recurringOrder.getMenuItemId().getSubscriptionId().getDietitianId().getLastName())
                            .orderStatus(lookupRepository.findByLookupValueCode(recurringOrder.getOrderStatus().getLookupValueCode()).getLookupValue())
                            .orderStatusCode(recurringOrder.getOrderStatus().getLookupValueCode())
                            .build());
                    duplicates.put(key, 1);
                }
            }
        });
        return recurringOrderResponses;
    }

    public List<RecurringOrderDetailsResponse> getNewRecurringOrdersForDietitianDetails(String orderNumber){
        long orderNumb = Integer.parseInt(orderNumber.replace("REC-ORDER-",""));

        List<RecurringOrders> recurringOrders = recurringOrderRepository.findByOrderNumber(orderNumb);
        List<RecurringOrderDetailsResponse> recurringOrderResponses = new ArrayList<>();

        recurringOrders.forEach(recurringOrder -> {
            recurringOrderResponses.add(RecurringOrderDetailsResponse.builder()
                    .restaurantName(recurringOrder.getRestaurantId().getRestaurantProfile().getRestaurantName())
                    .itemName(recurringOrder.getMenuItemId().getParentItemId().getItemName())
                    .childItems(recurringOrder.getMenuItemId().getChildItems())
                    .quantityAndUnit(recurringOrder.getMenuItemId().getQuantity()+" "+
                            recurringOrder.getMenuItemId().getQuantityUnit().getLookupValue())
                    .instructions(recurringOrder.getMenuItemId().getInstructions())
                    .fromDate(recurringOrder.getFromDate())
                    .toDate(recurringOrder.getToDate())
                    .deliveryTime(recurringOrder.getOrderDeliveryTime())
                    .price(recurringOrder.getOrderTotalPrice())
                    .build());
        });
        return recurringOrderResponses;
    }

    public void updateNewRecurringOrdersForRestaurantDetails(List<RecurringOrderDetailsResponse> recurringOrderDetailsRequest){
        recurringOrderDetailsRequest.forEach(req -> {
            long orderId = Integer.parseInt(req.getOrderId().replace("ORDER-",""));
            RecurringOrders recurringOrder = recurringOrderRepository.findById(orderId).get();
            recurringOrder.setOrderTotalPrice(req.getPrice());
            recurringOrder.setOrderStatus(lookupRepository.findByLookupValueCode(RecurringOrderStatus.REC_ORDER_STATUS_2.name()));
            recurringOrderRepository.save(recurringOrder);
        });
    }

    public void updateNewRecurringOrdersByDietitian(String orderNumber){
        long orderNumb = Integer.parseInt(orderNumber.replace("REC-ORDER-",""));
        List<RecurringOrders> recurringOrders = recurringOrderRepository.findByOrderNumber(orderNumb);
        recurringOrders.forEach(recurringOrders1 -> recurringOrders1.setOrderStatus(lookupRepository.findByLookupValueCode(RecurringOrderStatus.REC_ORDER_STATUS_3.name())));
        recurringOrderRepository.saveAll(recurringOrders);
    }

    public void confirmNewRecurringOrdersByRestaurant(String orderNumber){
        long orderNumb = Integer.parseInt(orderNumber.replace("REC-ORDER-",""));
        User restaurant = getCurrentLoggedUserDetails();
        List<RecurringOrders> recurringOrders = recurringOrderRepository.findByRestaurantIdAndOrderNumber(restaurant, orderNumb);
        recurringOrders.forEach(recurringOrders1 -> recurringOrders1.setOrderStatus(lookupRepository.findByLookupValueCode(RecurringOrderStatus.REC_ORDER_STATUS_4.name())));
        recurringOrderRepository.saveAll(recurringOrders);
    }


    public List<RecurringOrderResponse> getRecurringOrdersCreatedByDietitian(){
        User dietitian = getCurrentLoggedUserDetails();

        List<Object[]> recurringOrders = recurringOrderRepository.getRecurringOrderDetailsForDietitian(dietitian.getUserName());
        List<RecurringOrderResponse> recurringOrderResponses = new ArrayList<>();

        recurringOrders.forEach(recurringOrder -> {
            recurringOrderResponses.add(RecurringOrderResponse.builder()
                    .orderId("REC-ORDER-" + AppUtils.castObjectToString(recurringOrder[0]))
                    .customerName(AppUtils.castObjectToString(recurringOrder[1])+", "+AppUtils.castObjectToString(recurringOrder[2]))
                    .customerUsername(AppUtils.castObjectToString(recurringOrder[3]))
                    .orderStatus(AppUtils.castObjectToString(recurringOrder[4]))
                    .orderStatusCode(AppUtils.castObjectToString(recurringOrder[5]))
                    .build());
        });
        return recurringOrderResponses;
    }
}
