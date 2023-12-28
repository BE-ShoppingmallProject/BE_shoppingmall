package com.github.shoppingmall.shopping_mall.web.dto.seller_item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.sql.In;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemDto {
    private Integer itemId;
    private String email;
    private Integer categoryId;
    private String itemName;
    private String description;
    private Integer unitPrice;
}
