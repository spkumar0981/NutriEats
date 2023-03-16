package com.nutri.rest.repository;

import com.nutri.rest.model.ChildItem;
import com.nutri.rest.model.ParentItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChildItemRepository extends JpaRepository<ChildItem, Long> {
    ChildItem findByItemName(String itemName);
    List<ChildItem> findByParentItem(ParentItem parentItem);
}
