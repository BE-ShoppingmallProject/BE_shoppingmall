package com.github.shoppingmall.shopping_mall.repository.SellerItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerStockItemRepository extends JpaRepository<SellerStockItem, Integer> {

}
