package com.github.shoppingmall.shopping_mall.repository.Item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.itemOptions LEFT JOIN FETCH i.stockItems " +
            "WHERE (:type = 'name' AND i.itemName LIKE %:keyword%) " +
            "OR (:type = 'content' AND i.itemExplain LIKE %:keyword%)")
    Page<Item> searchByTypeWithDetails(@Param("type") String type,
                                       @Param("keyword") String keyword,
                                       Pageable pageable);
}
