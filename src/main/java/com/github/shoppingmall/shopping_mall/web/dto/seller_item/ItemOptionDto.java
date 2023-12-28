package com.github.shoppingmall.shopping_mall.web.dto.seller_item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemOptionDto {
    private Integer optionId;
    private String content;
    private Integer addPrice;
}
