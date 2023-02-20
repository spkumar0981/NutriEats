package com.nutri.rest.service;

import com.nutri.rest.mapper.ItemMapper;
import com.nutri.rest.repository.*;
import com.nutri.rest.response.ItemDetailsResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final SubscriptionRepository subscriptionRepository;

    private final UserRepository userRepository;

    private final LookupRepository lookupRepository;

    private final MenuItemRepository menuItemRepository;

    private final ItemRepository itemRepository;

    public ItemService(SubscriptionRepository subscriptionRepository, UserRepository userRepository, LookupRepository lookupRepository, MenuItemRepository menuItemRepository, ItemRepository itemRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.lookupRepository = lookupRepository;
        this.menuItemRepository = menuItemRepository;
        this.itemRepository = itemRepository;
    }

    public List<ItemDetailsResponse> getAllItems(){
        List<Object[]> items = itemRepository.getAllItemsWithUnitCodes();
        if(CollectionUtils.isEmpty(items)){
            return new ArrayList<>();
        }
        return items.stream().map(ItemMapper::mapItemDetailsFromObjArray).collect(Collectors.toList());
    }
}
