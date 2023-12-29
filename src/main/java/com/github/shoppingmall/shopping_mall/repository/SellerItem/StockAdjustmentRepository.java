package com.github.shoppingmall.shopping_mall.repository.SellerItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StockAdjustmentRepository extends JpaRepository<StockAdjustment, Integer> {
}
