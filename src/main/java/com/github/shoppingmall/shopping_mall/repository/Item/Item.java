package com.github.shoppingmall.shopping_mall.repository.Item;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.github.shoppingmall.shopping_mall.repository.SellerItem.StockItem;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "ITEM")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Integer itemId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "unit_price")
    private Integer unitPrice;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "item_explain")
    private String itemExplain;

    @OneToMany(mappedBy = "item")
    private Set<ItemOption> itemOptions;

    @OneToMany(mappedBy = "item")
    private Set<StockItem> stockItems;
}
