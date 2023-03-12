package com.nutri.rest.repository;

import com.nutri.rest.model.ChildItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChildItemRepository extends JpaRepository<ChildItem, Long> {
    ChildItem findByItemName(String itemName);
}
