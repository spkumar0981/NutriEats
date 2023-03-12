package com.nutri.rest.repository;

import com.nutri.rest.model.ParentItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParentItemRepository extends JpaRepository<ParentItem, Long> {
    ParentItem findByItemName(String itemName);

    @Query(value = "SELECT ITEM_DET.ITEM_NAME, LISTAGG(LV.LOOKUP_VALUE_CODE || ', ' || LV.LOOKUP_VALUE, '; ') " +
            "FROM PARENT_ITEM ITEM_DET " +
            "LEFT OUTER JOIN LOOKUP_VALUE LV ON (LV.LOOKUP_VALUE_TYPE=ITEM_DET.LOOKUP_VALUE_TYPE_OF_ITEM_UNIT) " +
            "GROUP BY ITEM_DET.ITEM_NAME", nativeQuery = true)
    List<Object[]> getAllItemsWithUnitCodes();
}
