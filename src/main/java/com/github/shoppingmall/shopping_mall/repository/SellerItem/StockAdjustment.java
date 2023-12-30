package com.github.shoppingmall.shopping_mall.repository.SellerItem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.shoppingmall.shopping_mall.repository.Item.Item;
import com.github.shoppingmall.shopping_mall.repository.Item.ItemOption;
import com.github.shoppingmall.shopping_mall.repository.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "STOCK_ADJUSTMENT")
public class StockAdjustment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adjustment_id")
    private Integer adjustmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    @JsonIgnore
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = true)
    @JsonIgnore
    private ItemOption option;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name ="reason")
    private String reason;

    @Column(name = "adjustment_date")
    private Timestamp adjustmentDate;
}
