package com.nutri.rest.service;

import com.nutri.rest.exception.EntityAlreadyExistException;
import com.nutri.rest.mapper.RestaurantMapper;
import com.nutri.rest.model.*;
import com.nutri.rest.repository.*;
import com.nutri.rest.request.RestaurantItemsRequest;
import com.nutri.rest.response.ItemDetailsResponse;
import com.nutri.rest.response.RestaurantItemsResponse;
import com.nutri.rest.response.RestaurantListResponse;
import com.nutri.rest.utils.ItemWeightsAndPrices;
import com.nutri.rest.utils.UserRoles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    private final UserRepository userRepository;

    private final RestaurantItemsRepository restaurantItemsRepository;

    private final ChildItemRepository childItemRepository;
    private final RestaurantItemWeightsAndPricesRepository itemWeightsAndPricesRepository;
    private final LookupRepository lookupRepository;

    public RestaurantService(UserRepository userRepository, RestaurantItemsRepository restaurantItemsRepository, ChildItemRepository childItemRepository, RestaurantItemWeightsAndPricesRepository itemWeightsAndPricesRepository, LookupRepository lookupRepository) {
        this.userRepository = userRepository;
        this.restaurantItemsRepository = restaurantItemsRepository;
        this.childItemRepository = childItemRepository;
        this.itemWeightsAndPricesRepository = itemWeightsAndPricesRepository;
        this.lookupRepository = lookupRepository;
    }

    public Page<RestaurantListResponse> getAllRestaurants(Pageable pageable) {
        return userRepository.findByUserType(UserRoles.ROLE_RESTAURANT.name(), pageable).map(restaurant ->
                RestaurantMapper.mapFromUserDomainToResponse(restaurant));
    }

    public Collection<RestaurantItemsResponse> getRestaurantItemsWhenLogged() {
        User user = getCurrentLoggedUserDetails();
        List<RestaurantItems> items = restaurantItemsRepository.findByRestaurantId(user);
        return mapItemsToItemsResponse(items, user.getUserName());
    }

    public Collection<RestaurantItemsResponse> getRestaurantItemsWhenNotLogged(String restaurantUserName) {
        User user = userRepository.findByUserName(restaurantUserName).get();
        List<RestaurantItems> items = restaurantItemsRepository.findByRestaurantId(user);

        return mapItemsToItemsResponse(items, user.getUserName());
    }

    public void createRestaurantItems(RestaurantItemsRequest itemReq) {
        User user = getCurrentLoggedUserDetails();
        ChildItem item = childItemRepository.findByItemName(itemReq.getItemName());

        /*if(restaurantItemsRepository.findByRestaurantIdAndChildItemId(user, item)!=null)
            throw new EntityAlreadyExistException("Item with same name already exists");*/

        RestaurantItems restaurantItem = restaurantItemsRepository.findByRestaurantIdAndChildItemId(user, item);
        if(restaurantItem == null) {
            restaurantItem = RestaurantItems.builder()
                    .restaurantId(user)
                    .childItemId(item)
                    .itemDescription(itemReq.getItemDescription())
                    .availableFromTime(itemReq.getAvailableFromTime())
                    .availableToTime(itemReq.getAvailableToTime())
                    .itemImage(itemReq.getItemImage().getBytes())
                    .isActive("Y").build();
        }else {
            restaurantItem.setItemDescription(itemReq.getItemDescription());
            restaurantItem.setAvailableFromTime(itemReq.getAvailableFromTime());
            restaurantItem.setAvailableToTime(itemReq.getAvailableToTime());
            restaurantItem.setItemImage(itemReq.getItemImage().getBytes());
        }
        restaurantItem = restaurantItemsRepository.save(restaurantItem);

        LookupValue lookupValue = lookupRepository.findByLookupValueCode(itemReq.getQuantityUnit().getUnitLookupCode());

        RestaurantItemWeightsAndPrices itemWeightsAndPrices = itemWeightsAndPricesRepository.findByRestaurantItemIdAndQuantityAndQuantityUnit(restaurantItem,
                itemReq.getQuantity(), lookupValue);
        if(itemWeightsAndPrices!=null) {
            itemWeightsAndPrices.setItemPrice(itemReq.getItemPrice());
        }else{
            itemWeightsAndPrices = RestaurantItemWeightsAndPrices.builder()
                    .restaurantItemId(restaurantItem)
                    .itemPrice(itemReq.getItemPrice())
                    .quantityUnit(lookupValue)
                    .quantity(itemReq.getQuantity())
                    .build();
        }
        itemWeightsAndPricesRepository.save(itemWeightsAndPrices);
    }

    private User getCurrentLoggedUserDetails(){
        String loggedUserName = CurrentUserService.getLoggedUserName();
        return userRepository.findByUserName(loggedUserName).get();
    }

    private Collection<RestaurantItemsResponse> mapItemsToItemsResponse(List<RestaurantItems> items, String userName){
        Map<String, RestaurantItemsResponse> uniqueItems = new HashMap<>();
        for (RestaurantItems item: items) {
            String parentItemName = item.getChildItemId().getParentItem().getItemName();
            RestaurantItemsResponse resp = uniqueItems.get(parentItemName);
            List<RestaurantItemsResponse.ChildItems> childItems;
            if(resp == null) {
                childItems = new ArrayList<>();
                resp = RestaurantItemsResponse.builder()
                        .parentItemName(parentItemName)
                        .restaurantUserName(userName)
                        .childItems(childItems)
                        .build();
            }else {
                childItems = resp.getChildItems();
            }
            List<ItemWeightsAndPrices> itemWeightsAndPrices = itemWeightsAndPricesRepository.findByRestaurantItemId(item).stream()
                    .map(restaurantItemWeightsAndPrices -> ItemWeightsAndPrices.builder()
                            .quantity(restaurantItemWeightsAndPrices.getQuantity())
                            .quantityUnit(ItemDetailsResponse.LookupUnits.builder()
                                    .unitLookupCode(restaurantItemWeightsAndPrices.getQuantityUnit().getLookupValueCode())
                                    .unitLookupValue(restaurantItemWeightsAndPrices.getQuantityUnit().getLookupValue()).build())
                            .itemPrice(restaurantItemWeightsAndPrices.getItemPrice()).build()).collect(Collectors.toList());

            childItems.add(RestaurantItemsResponse.ChildItems.builder()
                    .itemName(item.getChildItemId().getItemName())
                    .availableFromTime(item.getAvailableFromTime())
                    .availableToTime(item.getAvailableToTime())
                    .itemDescription(item.getItemDescription())
                    .isActive(item.getIsActive())
                    .itemImage(new String(item.getItemImage(), StandardCharsets.UTF_8))
                    .itemCategory(item.getChildItemId().getItemCategory())
                    .itemWeightsAndPrices(itemWeightsAndPrices)
                    .build());
            uniqueItems.put(resp.getParentItemName(), resp);
        }

        return uniqueItems.values();
    }
}
