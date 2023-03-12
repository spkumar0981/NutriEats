package com.nutri.rest.service;

import com.nutri.rest.exception.EntityAlreadyExistException;
import com.nutri.rest.mapper.RestaurantMapper;
import com.nutri.rest.model.ChildItem;
import com.nutri.rest.model.RestaurantItems;
import com.nutri.rest.model.User;
import com.nutri.rest.repository.ChildItemRepository;
import com.nutri.rest.repository.RestaurantItemsRepository;
import com.nutri.rest.repository.UserRepository;
import com.nutri.rest.request.common.RestaurantItemsReqAndResp;
import com.nutri.rest.response.RestaurantListResponse;
import com.nutri.rest.utils.UserRoles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RestaurantService {

    private final UserRepository userRepository;

    private final RestaurantItemsRepository restaurantItemsRepository;

    private final ChildItemRepository childItemRepository;

    public RestaurantService(UserRepository userRepository, RestaurantItemsRepository restaurantItemsRepository, ChildItemRepository childItemRepository) {
        this.userRepository = userRepository;
        this.restaurantItemsRepository = restaurantItemsRepository;
        this.childItemRepository = childItemRepository;
    }

    public Page<RestaurantListResponse> getAllRestaurants(Pageable pageable) {
        return userRepository.findByUserType(UserRoles.ROLE_RESTAURANT.name(), pageable).map(restaurant ->
                RestaurantMapper.mapFromUserDomainToResponse(restaurant));
    }

    public Page<RestaurantItemsReqAndResp> getRestaurantItemsWhenLogged(Pageable pageable) {
        User user = getCurrentLoggedUserDetails();
        return restaurantItemsRepository.findByRestaurantId(user, pageable).map(restaurantItems ->
            RestaurantItemsReqAndResp.builder().itemName(restaurantItems.getChildItemId().getItemName())
                    .restaurantUserName(user.getUserName())
                    .availableFromTime(restaurantItems.getAvailableFromTime())
                    .availableToTime(restaurantItems.getAvailableToTime())
                    .itemPrice(restaurantItems.getItemPrice())
                    .itemDescription(restaurantItems.getItemDescription())
                    .isActive(restaurantItems.getIsActive())
                    .itemImage(restaurantItems.getChildItemId().getItemImage())
                    .itemCategory(restaurantItems.getChildItemId().getItemCategory())
                    .parentItemName(restaurantItems.getChildItemId().getParentItem().getItemName()).build()
        );
    }

    public Page<RestaurantItemsReqAndResp> getRestaurantItemsWhenNotLogged(String restaurantUserName, Pageable pageable) {
        User user = userRepository.findByUserName(restaurantUserName).get();
        return restaurantItemsRepository.findByRestaurantId(user, pageable).map(restaurantItems ->
                RestaurantItemsReqAndResp.builder().itemName(restaurantItems.getChildItemId().getItemName())
                        .restaurantUserName(user.getUserName())
                        .availableFromTime(restaurantItems.getAvailableFromTime())
                        .availableToTime(restaurantItems.getAvailableToTime())
                        .itemPrice(restaurantItems.getItemPrice())
                        .itemDescription(restaurantItems.getItemDescription())
                        .isActive(restaurantItems.getIsActive())
                        .itemImage(restaurantItems.getChildItemId().getItemImage())
                        .itemCategory(restaurantItems.getChildItemId().getItemCategory())
                        .parentItemName(restaurantItems.getChildItemId().getParentItem().getItemName()).build()
        );
    }

    public void createRestaurantItems(RestaurantItemsReqAndResp itemReq, Pageable pageable) {
        User user = getCurrentLoggedUserDetails();
        ChildItem item = childItemRepository.findByItemName(itemReq.getItemName());
        if(restaurantItemsRepository.findByRestaurantIdAndChildItemId(user, item)!=null)
            throw new EntityAlreadyExistException("Item already exists");

        RestaurantItems restaurantItem = RestaurantItems.builder()
                .restaurantId(user)
                .childItemId(item)
                .itemPrice(itemReq.getItemPrice())
                .itemDescription(itemReq.getItemDescription())
                .availableFromTime(itemReq.getAvailableFromTime())
                .availableToTime(itemReq.getAvailableToTime())
                .isActive(itemReq.getIsActive()).build();
        restaurantItemsRepository.save(restaurantItem);
    }

    private User getCurrentLoggedUserDetails(){
        String loggedUserName = CurrentUserService.getLoggedUserName();
        return userRepository.findByUserName(loggedUserName).get();
    }
}
