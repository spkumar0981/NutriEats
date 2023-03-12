package com.nutri.rest.service;

import com.nutri.rest.mapper.CustomerMapper;
import com.nutri.rest.mapper.DietitianMapper;
import com.nutri.rest.mapper.ItemMapper;
import com.nutri.rest.model.*;
import com.nutri.rest.repository.*;
import com.nutri.rest.request.DietitianRequest;
import com.nutri.rest.request.ItemRequest;
import com.nutri.rest.response.*;
import com.nutri.rest.utils.SubscriptionStatus;
import com.nutri.rest.utils.UserRoles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.nutri.rest.utils.SubscriptionStatus.SUBSCRIPTION_STATUS_2;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    private final UserRepository userRepository;

    private final LookupRepository lookupRepository;

    private final MenuItemRepository menuItemRepository;

    private final ParentItemRepository parentItemRepository;
    private final RatingRepository ratingRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, UserRepository userRepository, LookupRepository lookupRepository, MenuItemRepository menuItemRepository, ParentItemRepository parentItemRepository, RatingRepository ratingRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.lookupRepository = lookupRepository;
        this.menuItemRepository = menuItemRepository;
        this.parentItemRepository = parentItemRepository;
        this.ratingRepository = ratingRepository;
    }

    public Page<DietitianListResponse> getAllDietitians(Pageable pageable) {
        User customer = getCurrentLoggedUserDetails();
        return userRepository.findByUserType(UserRoles.ROLE_DIETITIAN.name(), pageable).map(dietitian -> {
            //to check if customer is dietitian is already mapped to current customer
            Subscription subscription = subscriptionRepository.findByCustomerIdAndDietitianId(customer, dietitian);
            Double dietitianRating = ratingRepository.avgRating(dietitian.getId());
            return DietitianMapper.mapFromUserDomainToResponse(dietitian, subscription, dietitianRating);
        });
    }

    public Page<CustomerListResponse> getAllCustomers(Pageable pageable) {
        return userRepository.findByUserType(UserRoles.ROLE_CUSTOMER.name(), pageable)
                .map(dietitian -> CustomerMapper.mapCustomerDetails(dietitian));
    }

    public Page<CustomerListResponse> getAllCustomersForADietitian(Pageable pageable) {
        User dietitian = getCurrentLoggedUserDetails();
        return userRepository.getAllCustomersForADietitian(dietitian.getUserName(), pageable).map(CustomerMapper::mapCustomerDetailsFromObjArray);
    }

    public Page<CustomerListResponse> getAllNewCustomersForADietitian(Pageable pageable) {
        User dietitian = getCurrentLoggedUserDetails();
        return userRepository.getAllNewCustomersForADietitian(dietitian.getUserName(), SubscriptionStatus.SUBSCRIPTION_STATUS_1.name(), SubscriptionStatus.SUBSCRIPTION_STATUS_3.name(), SubscriptionStatus.SUBSCRIPTION_STATUS_4.name(), pageable)
                .map(CustomerMapper::mapCustomerDetailsFromObjArray);
    }

    public Page<DietitianListResponse> getAllHiredDietitiansOfACustomer(Pageable pageable) {
        User customer = getCurrentLoggedUserDetails();
        return userRepository.getAllHiredDietitiansOfCustomer(customer.getUserName(), pageable).map(
                DietitianMapper::mapDietitianDetailsFromObjArray);
    }

    public String sendMessageToCustomer(DietitianRequest customerRequest) {
        User dietitian = getCurrentLoggedUserDetails();
        User customer = userRepository.findByUserName(customerRequest.getUserName()).get();
        Subscription subscription = subscriptionRepository.findByCustomerIdAndDietitianId(customer, dietitian);
        subscription.setDietitianInput(customerRequest.getDietitianInput());
        subscriptionRepository.save(subscription);
        return "Message sent successfully";
    }

    public String sendMessageToDietitian(DietitianRequest dietitianRequest) {
        User customer = getCurrentLoggedUserDetails();
        User dietitian = userRepository.findByUserName(dietitianRequest.getUserName()).get();
        Subscription subscription = subscriptionRepository.findByCustomerIdAndDietitianId(customer, dietitian);
        subscription.setCustomerInput(dietitianRequest.getCustomerInput());
        subscriptionRepository.save(subscription);
        return "Message sent successfully";
    }

    public String hireDietitian(DietitianRequest dietitianRequest) {
        User customer = getCurrentLoggedUserDetails();
        User dietitian = userRepository.findByUserName(dietitianRequest.getUserName()).get();
        LookupValue subscribedStatus = lookupRepository.findByLookupValueCode(SubscriptionStatus.SUBSCRIPTION_STATUS_1.name());
        LookupValue preferredMealOption = lookupRepository.findByLookupValueCode(dietitianRequest.getPreferredMealOption().getUnitLookupCode());
        Subscription subscription = Subscription.builder()
                .customerId(customer)
                .dietitianId(dietitian)
                .status(subscribedStatus)
                .customerInput(dietitianRequest.getCustomerInput())
                .subscriptionExpireDate(LocalDate.now().plusWeeks(1))
                .preferredMealOption(preferredMealOption)
                .build();
        subscriptionRepository.save(subscription);
        return "Dietitian hired successfully";
    }

    public String confirmDietitianMenu(DietitianRequest dietitianRequest) {
        User customer = getCurrentLoggedUserDetails();
        User dietitian = userRepository.findByUserName(dietitianRequest.getUserName()).get();
        LookupValue subscribedStatus = lookupRepository.findByLookupValueCode(SubscriptionStatus.SUBSCRIPTION_STATUS_4.name());
        Subscription subscription = subscriptionRepository.findByCustomerIdAndDietitianId(customer, dietitian);
        subscription.setCustomerInput(dietitianRequest.getCustomerInput());
        subscription.setStatus(subscribedStatus);
        subscriptionRepository.save(subscription);
        return "Menu Confirmed successfully";
    }

    public String rejectDietitianMenu(DietitianRequest dietitianRequest) {
        User customer = getCurrentLoggedUserDetails();
        User dietitian = userRepository.findByUserName(dietitianRequest.getUserName()).get();
        LookupValue subscribedStatus = lookupRepository.findByLookupValueCode(SubscriptionStatus.SUBSCRIPTION_STATUS_3.name());
        Subscription subscription = subscriptionRepository.findByCustomerIdAndDietitianId(customer, dietitian);
        subscription.setCustomerInput(dietitianRequest.getCustomerInput());
        subscription.setStatus(subscribedStatus);
        subscriptionRepository.save(subscription);
        return "Menu Rejected successfully";
    }

    public List<ItemResponse> getItemsForASubscriptionForCustomer(String customerName){
        User dietitian = getCurrentLoggedUserDetails();
        User customer = userRepository.findByUserName(customerName).get();
        Subscription subscription = subscriptionRepository.findByCustomerIdAndDietitianId(customer, dietitian);
        List<MenuItem> menuItems = menuItemRepository.findBySubscriptionId(subscription);
        if(CollectionUtils.isEmpty(menuItems)){
            return new ArrayList<>();
        }
        return menuItems.stream().map(ItemMapper::mapToItems).collect(Collectors.toList());
    }

    public List<ItemDetailsResponse.LookupUnits> getItemUnits(String itemName){
        ParentItem parentItem = parentItemRepository.findByItemName(itemName);
        return lookupRepository.findByLookupValueType(parentItem.getLookupValueTypeOfItemUnit()).stream().map(lookupValue -> ItemDetailsResponse.LookupUnits.builder()
                .unitLookupValue(lookupValue.getLookupValue())
                .unitLookupCode(lookupValue.getLookupValueCode()).build()).collect(Collectors.toList());
    }

    public List<ItemResponse> getItemsForASubscriptionForDietitian(String dietitianName){
        User customer = getCurrentLoggedUserDetails();
        User dietitian = userRepository.findByUserName(dietitianName).get();
        Subscription subscription = subscriptionRepository.findByCustomerIdAndDietitianId(customer, dietitian);
        List<MenuItem> menuItems = menuItemRepository.findBySubscriptionId(subscription);
        if(CollectionUtils.isEmpty(menuItems)){
            return new ArrayList<>();
        }
        return menuItems.stream().map(ItemMapper::mapToItems).collect(Collectors.toList());
    }

    public ItemResponse addOrUpdateItemToSubscription(String customerName, ItemRequest itemRequest){
        User dietitian = getCurrentLoggedUserDetails();
        User customer = userRepository.findByUserName(customerName).get();
        Subscription subscription = subscriptionRepository.findByCustomerIdAndDietitianId(customer, dietitian);
        ParentItem parentItem = parentItemRepository.findByItemName(itemRequest.getItemName());
        LookupValue lookupValue = lookupRepository.findByLookupValueCode(itemRequest.getQuantityUnit().getUnitLookupCode());
        MenuItem menuItem = menuItemRepository.findBySubscriptionIdAndParentItemId(subscription, parentItem);
        if(menuItem!=null){
            menuItem.setQuantity(itemRequest.getQuantity());
            menuItem.setQuantityUnit(lookupValue);
            menuItem.setIsActive("Y");
        }else {
            menuItem = MenuItem.builder()
                    .subscriptionId(subscription)
                    .parentItemId(parentItem)
                    .quantity(itemRequest.getQuantity())
                    .quantityUnit(lookupValue)
                    .isActive("Y")
                    .build();
        }
        menuItemRepository.save(menuItem);
        return ItemResponse.builder()
                .itemName(itemRequest.getItemName())
                .quantityUnit(itemRequest.getQuantityUnit().getUnitLookupValue())
                .quantity(itemRequest.getQuantity()).build();
    }

    public ResponseText deleteItemInSubscription(String customerName, String itemName){
        User dietitian = getCurrentLoggedUserDetails();
        User customer = userRepository.findByUserName(customerName).get();
        Subscription subscription = subscriptionRepository.findByCustomerIdAndDietitianId(customer, dietitian);
        ParentItem parentItem = parentItemRepository.findByItemName(itemName);
        MenuItem menuItem = menuItemRepository.findBySubscriptionIdAndParentItemId(subscription, parentItem);
        menuItem.setIsActive("N");
        menuItemRepository.delete(menuItem);
        return ResponseText.builder()
                .response("ParentItem deleted successfully").build();
    }

    public void confirmMealForASubscription(DietitianRequest customerReq){
        User dietitian = getCurrentLoggedUserDetails();
        User customer = userRepository.findByUserName(customerReq.getUserName()).get();
        Subscription subscription = subscriptionRepository.findByCustomerIdAndDietitianId(customer, dietitian);
        LookupValue lookupValue = lookupRepository.findByLookupValueCode(SUBSCRIPTION_STATUS_2.name());
        subscription.setStatus(lookupValue);
        subscription.setDietitianInput(customerReq.getDietitianInput());
        subscription.setAmount(customerReq.getSubscriptionAmount());
        subscriptionRepository.save(subscription);
    }

    private User getCurrentLoggedUserDetails(){
        String loggedUserName = CurrentUserService.getLoggedUserName();
        return userRepository.findByUserName(loggedUserName).get();
    }
}
