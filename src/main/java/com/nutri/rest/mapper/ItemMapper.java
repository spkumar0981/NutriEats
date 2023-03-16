package com.nutri.rest.mapper;

import com.nutri.rest.model.ChildItem;
import com.nutri.rest.model.ParentItem;
import com.nutri.rest.model.MenuItem;
import com.nutri.rest.response.ItemDetailsResponse;
import com.nutri.rest.response.ItemResponse;
import lombok.experimental.UtilityClass;

import java.util.*;

import static com.nutri.rest.utils.AppUtils.*;

@UtilityClass
public class ItemMapper {
    public ItemResponse mapToItems(MenuItem menuItem){
        return ItemResponse
                .builder()
                .itemName(menuItem.getParentItemId().getItemName())
                .childItems(menuItem.getChildItems())
                .quantity(menuItem.getQuantity())
                .quantityUnit(menuItem.getQuantityUnit().getLookupValue())
                .instructions(menuItem.getInstructions())
                .build();
    }

    public ItemDetailsResponse mapToItemDetailsResponse(ParentItem parentItem){
        return ItemDetailsResponse.builder()
                .itemName(parentItem.getItemName())
                .build();
    }

    public ItemDetailsResponse mapItemDetailsFromObjArray(Object[] item){

        List<ItemDetailsResponse.LookupUnits> lookupUnitsList = new ArrayList<>();
        String[] objects = castObjectToStringArray(item[1]);
        Arrays.stream(objects).spliterator().forEachRemaining(val -> {
            String[] valueAndCode = val.split(",");
            ItemDetailsResponse.LookupUnits lookupUnit = ItemDetailsResponse.LookupUnits.builder()
                    .unitLookupCode(valueAndCode[0].trim())
                    .unitLookupValue(valueAndCode[1].trim()).build();
            lookupUnitsList.add(lookupUnit);
        });
        return ItemDetailsResponse.builder()
                .itemName(castObjectToString(item[0]))
                .itemUnitsAndCodes(lookupUnitsList).build();
    }

    public ItemDetailsResponse mapChildItemDetailsToResponse(ChildItem item){

        return ItemDetailsResponse.builder()
                .itemName(item.getItemName()).build();
    }
}
