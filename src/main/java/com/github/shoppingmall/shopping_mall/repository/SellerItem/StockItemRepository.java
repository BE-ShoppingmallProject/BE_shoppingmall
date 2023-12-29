package com.github.shoppingmall.shopping_mall.repository.SellerItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockItemRepository extends JpaRepository<StockItem, Integer> {

    StockItem findByItem_ItemIdAndOption_OptionId(Integer itemId, Integer optionId);
}
