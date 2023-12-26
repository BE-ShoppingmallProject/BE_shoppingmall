package com.github.shoppingmall.shopping_mall.repository.SellerItem;

import com.github.shoppingmall.shopping_mall.repository.Item.Item;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "STOCK_ITEM")
public class SellerStockItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    private Integer stockId;

    //@ManyToOne
    //@JoinColumn(name = "user_id", nullable = false)
    //private User user;
    @Column(name = "user_id")
    private Integer userId;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "quantity")
    private Integer quantity;
}
