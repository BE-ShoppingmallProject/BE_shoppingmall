package com.github.shoppingmall.shopping_mall.repository.Item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemOptionRepository extends JpaRepository<ItemOption, Integer> {

    List<ItemOption> findByItem_ItemId(Integer itemId);
}
