package com.nutri.rest.service;

import com.nutri.rest.mapper.ItemMapper;
import com.nutri.rest.model.ChildItem;
import com.nutri.rest.model.ParentItem;
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

    private final ParentItemRepository parentItemRepository;

    private final ChildItemRepository childItemRepository;

    public ItemService(SubscriptionRepository subscriptionRepository, UserRepository userRepository, LookupRepository lookupRepository, MenuItemRepository menuItemRepository, ParentItemRepository parentItemRepository, ChildItemRepository childItemRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.lookupRepository = lookupRepository;
        this.menuItemRepository = menuItemRepository;
        this.parentItemRepository = parentItemRepository;
        this.childItemRepository = childItemRepository;
    }

    public List<ItemDetailsResponse> getAllItems(){
        List<Object[]> items = parentItemRepository.getAllItemsWithUnitCodes();
        if(CollectionUtils.isEmpty(items)){
            return new ArrayList<>();
        }
        return items.stream().map(ItemMapper::mapItemDetailsFromObjArray).collect(Collectors.toList());
    }

    public List<ItemDetailsResponse> getChildItemsOfParent(String parentItemName){
        ParentItem item = parentItemRepository.findByItemName(parentItemName);
        List<ChildItem> childItems = childItemRepository.findByParentItem(item);
        if(childItems==null)
            return new ArrayList<>();
        return childItems.stream().map(ItemMapper::mapChildItemDetailsToResponse).collect(Collectors.toList());
    }
}
