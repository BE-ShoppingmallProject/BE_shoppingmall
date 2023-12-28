package com.github.shoppingmall.shopping_mall.repository.Item;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

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
    private Integer itemId; // 상품 ID

    @Column(name = "user_id")
    private Integer userId; // 회원 ID

    @Column(name = "category_id")
    private Integer categoryId; // 카테고리 ID

    @Column(name = "item_name")
    private String itemName; // 상품이름

    @Column(name = "item_explain")
    private String itemExplain; // 상품 설명

    @Column(name = "item_count")
    private Integer itemCount; // 상품 개수

    @Enumerated(EnumType.STRING)
    @Column(name = "item_status")
    private ItemStatus itemStatus; // 상품상태(판매중, 품절)

    @Column(name = "unit_price")
    private Integer unitPrice; // 단가
}
