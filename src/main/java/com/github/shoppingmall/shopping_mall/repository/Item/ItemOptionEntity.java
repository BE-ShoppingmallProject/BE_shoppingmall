package com.github.shoppingmall.shopping_mall.repository.Item;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "ITEM_OPTION")
@IdClass(ItemOption.class)
public class ItemOptionEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Integer optionId;

    @Id
    @Column(name = "item_id")
    private Integer itemId;

    @Column(name = "option_content")
    private String optionContent;

    @Column(name = "additional_price")
    private Integer additionalPrice;
}
